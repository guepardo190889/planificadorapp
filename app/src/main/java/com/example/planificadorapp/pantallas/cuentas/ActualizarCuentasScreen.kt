package com.example.planificadorapp.pantallas.cuentas

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.DineroTextField
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.composables.cuentas.CuentasListConCheckbox
import com.example.planificadorapp.modelos.cuentas.ActualizarCuentaRequestModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.repositorios.CuentasRepository
import kotlinx.coroutines.launch
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

    var cuenta by remember {
        mutableStateOf<CuentaModel?>(
            null
        )
    }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf<BigDecimal>(BigDecimal.ZERO) }
    var isCuentaAgrupadora by remember { mutableStateOf(false) }

    var isNombreValido by remember { mutableStateOf(true) }
    var isSaldoValido by remember { mutableStateOf(true) }
    var isCuentasMezcladasValido by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

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
        return saldo > BigDecimal.ZERO
    }

    /**
     * Valida si al menos una cuenta disponible para agrupar están seleccionadas
     */
    fun validarCuentasMezcladas(): Boolean {
        return if (isCuentaAgrupadora) {
            cuentasMezcladas.any { it.seleccionada }
        } else {
            true
        }
    }

    /**
     * Valida la pantalla actual
     */
    fun validarPantalla(): Boolean {
        isNombreValido = validarNombre()
        isSaldoValido = validarSaldo()
        isCuentasMezcladasValido = validarCuentasMezcladas()
        return isNombreValido && isSaldoValido && isCuentasMezcladasValido
    }

    LaunchedEffect(Unit) {
        cuentasRepository.buscarCuentaPorId(idCuenta) { resultadoCuentaExistente ->
            if (resultadoCuentaExistente != null) {
                Log.i("CuentasScreen", "Activo encontrado: $resultadoCuentaExistente")
                cuenta = resultadoCuentaExistente
                nombre = resultadoCuentaExistente.nombre
                descripcion = resultadoCuentaExistente.descripcion ?: ""
                saldo = resultadoCuentaExistente.saldo
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

                            cuentasMezcladas += cuentasAgrupadas
                            cuentasMezcladas += cuentasNoAgrupadorasSinAgrupar

                            Log.i(
                                "ActualizarCuentasScreen",
                                "Cuentas mezcladas: ${cuentasMezcladas.size}"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Actualiza la cuenta en la base de datos
     */
    fun actualizarCuenta() {
        var cuentasParaAgrupar = emptyList<Long>()
        if (isCuentaAgrupadora) {
            cuentasParaAgrupar = cuentasMezcladas.filter { it.seleccionada }
                .map { it.id }
        }

        cuentasRepository.actualizarCuenta(
            idCuenta, ActualizarCuentaRequestModel(nombre, descripcion, saldo, cuentasParaAgrupar)
        ) {
            if (it != null) {
                snackbarMessage = "Cuenta actualizada exitosamente"
                snackbarType = "success"
            } else {
                snackbarMessage = "Error al actualizar la cuenta"
            }

            coroutineScope.launch {
                snackbarHostState.showSnackbar(snackbarMessage)

                if (it != null) {
                    navController.navigate("cuentas")
                }
            }
        }
    }

    Scaffold(modifier, snackbarHost = {
        SnackbarHost(snackbarHostState) {
            SnackBarConColor(
                snackbarHostState = snackbarHostState, tipo = snackbarType
            )
        }
    }, bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth(), content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        if (validarPantalla()) {
                            actualizarCuenta()
                        }
                    }) {
                    Icon(
                        Icons.Default.Done, contentDescription = "Actualizar Cuenta"
                    )
                }
            }
        })
    }, content = { paddingValues ->
        Column(
            modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextoConEtiqueta(
                etiqueta = "¿Es Cuenta Agrupadora?: ",
                texto = if (isCuentaAgrupadora) "Si" else "No",
                styleLabel = "medium",
                styleBody = "medium"
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    isNombreValido = validarNombre()

                    if (it.length <= 32) {
                        nombre = it
                    }
                },
                label = { Text("Nombre") },
                isError = !isNombreValido,
                modifier = modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 2,
                textStyle = MaterialTheme.typography.bodyLarge,
                supportingText = {
                    if (!isNombreValido) {
                        Text(
                            text = "El nombre es requerido",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Text(
                        text = "${nombre.length}/32",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                },
                colors = OutlinedTextFieldDefaults.colors()
            )

            if (!isCuentaAgrupadora) {
                DineroTextField(modifier = modifier,
                    saldoInicial = saldo,
                    etiqueta = "Saldo",
                    mensajeError = "El saldo es requerido",
                    isSaldoValido = isSaldoValido,
                    onSaldoChange = {
                        saldo = it
                    })
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    if (it.length <= 128) {
                        descripcion = it
                    }
                },
                label = { Text("Descripción") },
                modifier = modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                textStyle = MaterialTheme.typography.bodyLarge,
                supportingText = {
                    Text(
                        text = "${descripcion.length}/128",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                },
                colors = OutlinedTextFieldDefaults.colors()
            )

            if (isCuentaAgrupadora) {
                Text(
                    text = "Cuentas Agrupadas: "
                )

                CuentasListConCheckbox(
                    modifier,
                    cuentasMezcladas,
                    onCuentaChequeada = { cuentaSeleccionada, isSeleccionada ->
                        isCuentasMezcladasValido = true
                        cuentasMezcladas = cuentasMezcladas.map { cuenta ->
                            if (cuenta.id == cuentaSeleccionada.id) {
                                cuenta.copy(seleccionada = isSeleccionada)
                            } else {
                                cuenta
                            }
                        }
                    }
                )

                if (!isCuentasMezcladasValido) {
                    Text(
                        text = "Debes seleccionar al menos una cuenta para agrupar",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    })
}