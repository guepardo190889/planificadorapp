package com.example.planificadorapp.pantallas.cuentas

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.planificadorapp.composables.cuentas.CuentasListConCheckbox
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.cuentas.GuardarCuentaRequestModel
import com.example.planificadorapp.repositorios.CuentasRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal

/**
 * Composable que representa la pantalla de guardado de una cuenta
 */
@Composable
fun GuardarCuentasScreen(modifier: Modifier, navController: NavController) {
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
        return if (!isCuentaAgrupadora) {
            saldo > BigDecimal.ZERO
        } else {
            true
        }
    }

    /**
     * Valida si al menos una cuenta disponible para agrupar están seleccionadas
     */
    fun validarCuentasNoAgrupadorasSinAgrupar(): Boolean {
        return if (isCuentaAgrupadora) {
            cuentasNoAgrupadorasSinAgrupar.any { it.seleccionada }
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
        isCuentasNoAgrupadorasSinAgruparValido = validarCuentasNoAgrupadorasSinAgrupar()
        return isNombreValido && isSaldoValido && isCuentasNoAgrupadorasSinAgruparValido
    }

    /**
     * Guarda la cuenta en la base de datos
     */
    fun guardarCuenta() {
        var cuentasAgrupadas = emptyList<Long>()
        if (isCuentaAgrupadora) {
            cuentasAgrupadas = cuentasNoAgrupadorasSinAgrupar.filter { it.seleccionada }
                .map { it.id }
        }

        cuentasRepository.guardarCuenta(
            GuardarCuentaRequestModel(
                nombre,
                descripcion,
                saldo,
                isCuentaAgrupadora,
                cuentasAgrupadas
            )
        ) { cuentaGuardada ->
            if (cuentaGuardada != null) {
                snackbarMessage = "Cuenta guardada exitosamente"
                snackbarType = "success"
            } else {
                snackbarMessage = "Error al guardar la cuenta"
                snackbarType = "error"
            }

            coroutineScope.launch {
                snackbarHostState.showSnackbar(snackbarMessage)

                if (cuentaGuardada != null) {
                    navController.navigate("cuentas")
                }
            }
        }
    }

    Scaffold(
        modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SnackBarConColor(
                    snackbarHostState = snackbarHostState,
                    tipo = snackbarType
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                if (validarPantalla()) {
                                    guardarCuenta()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = "Guardar Cuenta"
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
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
                    RadioButton(
                        selected = isCuentaAgrupadora,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
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
                        }
                    )

                    Text(text = "No", color = MaterialTheme.colorScheme.onSurface)
                    RadioButton(
                        selected = !isCuentaAgrupadora,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            isCuentaAgrupadora = !isCuentaAgrupadora
                        }
                    )
                }

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
                    DineroTextField(
                        modifier = modifier,
                        saldoInicial = saldo,
                        isSaldoValido = isSaldoValido,
                        onSaldoChange = {
                            saldo = it
                        }
                    )
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
                        text = "Selecciona las cuenta que deseas agrupar: "
                    )
                    CuentasListConCheckbox (
                        modifier,
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
                        }
                    )

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
    )
}