package com.example.planificadorapp.pantallas.cuentas

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.DineroTextField
import com.example.planificadorapp.composables.cuentas.CuentasListConCheckbox
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.composables.snackbar.SnackBarBase
import com.example.planificadorapp.composables.snackbar.SnackBarManager
import com.example.planificadorapp.composables.snackbar.SnackBarTipo
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.cuentas.GuardarCuentaRequestModel
import com.example.planificadorapp.repositorios.CuentasRepository
import java.math.BigDecimal

/**
 * Composable que representa la pantalla de guardado de una cuenta
 */
@Composable
fun GuardarCuentasScreen(
    modifier: Modifier, navController: NavController
) {
    val cuentasRepository = remember { CuentasRepository() }

    var cuentasNoAgrupadorasSinAgrupar by remember {
        mutableStateOf<List<CuentaModel>>(
            emptyList()
        )
    }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf<BigDecimal>(BigDecimal.ZERO) }

    var isCuentaAgrupadora by remember { mutableStateOf(false) }
    var isNombreValido by remember { mutableStateOf(true) }
    var isSaldoValido by remember { mutableStateOf(true) }
    var isCuentasNoAgrupadorasSinAgruparValido by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarManager = remember { SnackBarManager(coroutineScope, snackbarHostState) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isGuardando by remember { mutableStateOf(false) }

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
            saldo > BigDecimal.ZERO
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
        return isNombreValido && isSaldoValido && isCuentasNoAgrupadorasSinAgruparValido
    }

    /**
     * Guarda la cuenta en la base de datos
     */
    fun guardarCuenta() {
        isGuardando = true

        var cuentasAgrupadas = emptyList<Long>()
        if (isCuentaAgrupadora) {
            cuentasAgrupadas =
                cuentasNoAgrupadorasSinAgrupar.filter { it.seleccionada }.map { it.id }
        }

        cuentasRepository.guardarCuenta(
            GuardarCuentaRequestModel(
                nombre, descripcion, saldo, isCuentaAgrupadora, cuentasAgrupadas
            )
        ) { cuentaGuardada ->
            if (cuentaGuardada != null) {
                snackBarManager.mostrar("Cuenta guardada exitosamente", SnackBarTipo.SUCCESS) {
                    isGuardando = false
                    navController.navigate("cuentas")
                }
            } else {
                snackBarManager.mostrar("Error al guardar la cuenta", SnackBarTipo.ERROR) {
                    isGuardando = false
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(modifier, snackbarHost = {
        SnackBarBase(
            snackbarHostState = snackbarHostState, snackBarManager = snackBarManager
        )
    }, bottomBar = {
        BarraNavegacionInferior(isTransaccionGuardar = true, onTransaccionClick = {
            if (validarPantalla()) {
                guardarCuenta()
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Es una cuenta agrupadora?"
                    )

                    Spacer(modifier.padding(8.dp))

                    Text(text = "Si", color = MaterialTheme.colorScheme.onSurface)
                    RadioButton(selected = isCuentaAgrupadora, colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    ), onClick = {
                        isCuentaAgrupadora = !isCuentaAgrupadora
                        cuentasRepository.buscarCuentas(
                            excluirCuentasAsociadas = false,
                            incluirSoloCuentasNoAgrupadorasSinAgrupar = true
                        ) { cuentasEncontradas ->
                            cuentasNoAgrupadorasSinAgrupar = cuentasEncontradas ?: emptyList()
                            Log.i(
                                "GuardarCuentasScreen",
                                "Cuentas agrupadoras sin agrupar cargadas: ${cuentasNoAgrupadorasSinAgrupar.size}"
                            )
                        }
                    })

                    Text(text = "No", color = MaterialTheme.colorScheme.onSurface)
                    RadioButton(selected = !isCuentaAgrupadora, colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    ), onClick = {
                        isCuentaAgrupadora = !isCuentaAgrupadora
                    })
                }

                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = nombre,
                    onValueChange = {
                        isNombreValido = validarNombre()

                        if (it.length <= 32) {
                            nombre = it
                        }
                    },
                    label = { Text("Nombre") },
                    isError = !isNombreValido,
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
                    colors = OutlinedTextFieldDefaults.colors(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                if (!isCuentaAgrupadora) {
                    DineroTextField(modifier = modifier,
                        etiqueta = "Saldo",
                        mensajeError = "El saldo es requerido",
                        saldoInicial = saldo,
                        isSaldoValido = isSaldoValido,
                        onSaldoChange = {
                            saldo = it
                        },
                        onNextAction = {
                            focusManager.moveFocus(FocusDirection.Down)
                        })
                }

                OutlinedTextField(value = descripcion,
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
                    colors = OutlinedTextFieldDefaults.colors(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.clearFocus()
                    })
                )

                if (isCuentaAgrupadora) {
                    if (cuentasNoAgrupadorasSinAgrupar.isEmpty()) {
                        Text(
                            modifier = modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            text = "Sin cuentas disponibles para agrupar",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        Text(
                            text = "Selecciona las cuenta que deseas agrupar: "
                        )
                        CuentasListConCheckbox(modifier,
                            cuentasNoAgrupadorasSinAgrupar,
                            onCuentaChequeada = { cuentaSeleccionada, isSeleccionada ->
                                isCuentasNoAgrupadorasSinAgruparValido = true

                                cuentasNoAgrupadorasSinAgrupar =
                                    cuentasNoAgrupadorasSinAgrupar.map { cuenta ->
                                        if (cuenta.id == cuentaSeleccionada.id) {
                                            cuenta.copy(seleccionada = isSeleccionada)
                                        } else {
                                            cuenta
                                        }
                                    }
                            })

                        if (!isCuentasNoAgrupadorasSinAgruparValido) {
                            Text(
                                text = "Debes seleccionar al menos una cuenta para agrupar",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Si está guardando, mostramos un indicador de carga que bloquea toda la pantalla
            if (isGuardando) {
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