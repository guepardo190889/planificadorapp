package com.example.planificadorapp.pantallas.cuentas

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.cuentas.CuentasListConCheckbox
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.composables.snackbar.SnackBarBase
import com.example.planificadorapp.composables.snackbar.SnackBarManager
import com.example.planificadorapp.composables.snackbar.SnackBarTipo
import com.example.planificadorapp.composables.textfield.DineroTextField
import com.example.planificadorapp.composables.textfield.OutlinedTextFieldBase
import com.example.planificadorapp.modelos.cuentas.ActualizarCuentaRequestModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.navegacion.Ruta
import com.example.planificadorapp.repositorios.CuentasRepository
import java.math.BigDecimal

/**
 * Composable que representa la pantalla de actualizado de una cuenta
 */
@Composable
fun ActualizarCuentasScreen(modifier: Modifier, navController: NavController, idCuenta: Long) {
    val cuentasRepository = remember { CuentasRepository() }

    var cuentasAgrupadas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var cuentasNoAgrupadorasSinAgrupar by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var cuentasMezcladas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }

    var cuenta by remember { mutableStateOf<CuentaModel?>(null) }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf(BigDecimal.ZERO.toString()) }
    var isCuentaAgrupadora by remember { mutableStateOf(false) }

    var isNombreValido by remember { mutableStateOf(true) }
    var isSaldoValido by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarManager = remember { SnackBarManager(coroutineScope, snackbarHostState) }

    val nombreFocusRequester = remember { FocusRequester() }
    val descripcionFocusRequester = remember { FocusRequester() }
    val dineroFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isActualizando by remember { mutableStateOf(false) }

    /**
     * Valida si un nombre es válido
     */
    fun validarNombre(): Boolean {
        return nombre.isNotBlank()
    }

    /**
     * Valida si el saldo es válido
     */
    fun validarSaldo(): Boolean {
        return if (!isCuentaAgrupadora) {
            try {
                if (saldo.isNotEmpty()) {
                    BigDecimal(saldo.replace(",", "")) >= BigDecimal.ZERO
                } else {
                    true
                }
            } catch (e: NumberFormatException) {
                false
            }
        } else {
            true
        }
    }

    /**
     * Valida la pantalla actual
     */
    fun validarPantalla(): Boolean {
        isNombreValido = validarNombre()

        if (!isCuentaAgrupadora) {
            isSaldoValido = validarSaldo()
        }

        return isNombreValido && isSaldoValido
    }

    LaunchedEffect(Unit) {
        cuentasRepository.buscarCuentaPorId(idCuenta) { resultadoCuentaExistente ->
            if (resultadoCuentaExistente != null) {
                Log.i("CuentasScreen", "Activo encontrado: $resultadoCuentaExistente")
                cuenta = resultadoCuentaExistente
                nombre = resultadoCuentaExistente.nombre
                descripcion = resultadoCuentaExistente.descripcion ?: ""
                saldo = resultadoCuentaExistente.saldo.toString()
                isCuentaAgrupadora = resultadoCuentaExistente.agrupadora

                if (isCuentaAgrupadora) {
                    cuentasRepository.buscarCuentas(
                        excluirCuentasAsociadas = false,
                        incluirSoloCuentasNoAgrupadorasSinAgrupar = true
                    ) { cuentasEncontradas ->
                        cuentasNoAgrupadorasSinAgrupar = cuentasEncontradas ?: emptyList()
                        Log.i(
                            "ActualizarCuentasScreen",
                            "Cuentas no agrupadoras sin agrupar cargadas: ${cuentasNoAgrupadorasSinAgrupar.size}"
                        )

                        cuentasRepository.buscarSubcuentas(idCuenta) { cuentasAgrupadasEncontradas ->
                            cuentasAgrupadas = cuentasAgrupadasEncontradas?.map { cuenta ->
                                cuenta.copy(seleccionada = true)
                            } ?: emptyList()

                            Log.i(
                                "ActualizarCuentasScreen",
                                "Cuentas agrupadas: ${cuentasAgrupadas.size}"
                            )

                            cuentasMezcladas += cuentasAgrupadas + cuentasNoAgrupadorasSinAgrupar

                            Log.i(
                                "ActualizarCuentasScreen",
                                "Cuentas mezcladas: ${cuentasMezcladas.size}"
                            )
                        }
                    }
                }

                nombreFocusRequester.requestFocus()
                keyboardController?.show()
            }
        }
    }

    /**
     * Actualiza la cuenta en la base de datos
     */
    fun actualizarCuenta() {
        isActualizando = true

        var cuentasParaAgrupar = emptyList<Long>()
        if (isCuentaAgrupadora) {
            cuentasParaAgrupar = cuentasMezcladas.filter { it.seleccionada }.map { it.id }
        }

        val saldoActualizar = if (isCuentaAgrupadora) null else {
            if (saldo.isNotEmpty()) {
                BigDecimal(saldo.replace(",", ""))
            } else {
                BigDecimal.ZERO
            }
        }

        cuentasRepository.actualizarCuenta(
            idCuenta, ActualizarCuentaRequestModel(
                nombre, descripcion, saldoActualizar, cuentasParaAgrupar
            )
        ) { cuentaActualizada ->
            if (cuentaActualizada != null) {
                snackBarManager.mostrar("Cuenta actualizada exitosamente", SnackBarTipo.SUCCESS) {
                    isActualizando = false
                    navController.navigate(Ruta.CUENTAS.ruta)
                }
            } else {
                snackBarManager.mostrar("Error al actualizar la cuenta", SnackBarTipo.ERROR) {
                    isActualizando = false
                }
            }
        }
    }

    Scaffold(modifier, snackbarHost = {
        SnackBarBase(
            snackbarHostState = snackbarHostState, snackBarManager = snackBarManager
        )
    }, bottomBar = {
        BarraNavegacionInferior(isTransaccionGuardar = false, onTransaccionClick = {
            if (validarPantalla()) {
                actualizarCuenta()
            }
        })
    }, content = { paddingValues ->
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "¿Es Cuenta Agrupadora?: ${if (isCuentaAgrupadora) "Si" else "No"}",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextFieldBase(modifier = modifier.fillMaxWidth(),
                    value = nombre,
                    label = "Nombre",
                    maxLength = 32,
                    isError = !isNombreValido,
                    errorMessage = "El nombre es requerido",
                    focusRequester = nombreFocusRequester,
                    onValueChange = {
                        isNombreValido = validarNombre()

                        if (it.length <= 32) {
                            nombre = it
                        }
                    },
                    onNextAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })

                if (!isCuentaAgrupadora) {
                    DineroTextField(modifier = modifier,
                        monto = saldo,
                        etiqueta = "Saldo",
                        isError = !isSaldoValido,
                        focusRequester = dineroFocusRequester,
                        onSaldoChange = {
                            saldo = it
                        },
                        onNextAction = {
                            focusManager.moveFocus(FocusDirection.Down)
                        })
                }

                OutlinedTextFieldBase(modifier = modifier.fillMaxWidth(),
                    value = descripcion,
                    label = "Descripción",
                    maxLength = 128,
                    singleLine = false,
                    maxLines = 3,
                    focusRequester = descripcionFocusRequester,
                    onValueChange = {
                        if (it.length <= 128) {
                            descripcion = it
                        }
                    })

                if (isCuentaAgrupadora) {
                    if (cuentasMezcladas.isEmpty()) {
                        Text(
                            modifier = modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            text = "Sin cuentas disponibles para agrupar",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        Text(
                            text = "Cuentas Agrupadas: "
                        )

                        CuentasListConCheckbox(modifier,
                            cuentasMezcladas,
                            onCuentaChequeada = { cuentaSeleccionada, isSeleccionada ->
                                cuentasMezcladas = cuentasMezcladas.map { cuenta ->
                                    if (cuenta.id == cuentaSeleccionada.id) {
                                        cuenta.copy(seleccionada = isSeleccionada)
                                    } else {
                                        cuenta
                                    }
                                }
                            })
                    }
                }
            }

            // Si está guardando, mostramos un indicador de carga que bloquea toda la pantalla
            if (isActualizando) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                        .clickable(enabled = false) {}, contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    })
}