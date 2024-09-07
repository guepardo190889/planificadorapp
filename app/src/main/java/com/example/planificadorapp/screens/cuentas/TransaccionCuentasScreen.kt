package com.example.planificadorapp.screens.cuentas

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.composables.cuentas.CuentasDropDown
import com.example.planificadorapp.modelos.ValidacionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.cuentas.TransaccionCuentaRequestModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.utilerias.FormatoMonto
import kotlinx.coroutines.launch

/**
 * Composable que representa la pantalla de guardado/actualización de una cuenta
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaccionCuentasScreen(modifier: Modifier, idCuenta: Long, navController: NavController) {
    val cuentasRepository = remember { CuentasRepository() }

    var cuentasPadre by remember {
        mutableStateOf<List<CuentaModel>>(
            emptyList()
        )
    }
    var cuenta by remember {
        mutableStateOf<CuentaModel?>(
            null
        )
    }
    var descripcionBoton by remember { mutableStateOf("Guardar") }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf("") }
    var cuentaSeleccionada by remember {
        mutableStateOf<CuentaModel?>(
            null
        )
    }
    var cuentaPrincipalListaHabilitada by remember { mutableStateOf(true) }

    var isNombreValido by remember { mutableStateOf(true) }
    var isSaldoValido by remember { mutableStateOf(ValidacionModel()) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

    /**
     * Valida si un nombre es válido
     */
    fun validarNombre(): Boolean {
        return !nombre.isNullOrBlank()
    }

    /**
     * Valida si el saldo es válido
     */
    fun validarSaldo(): ValidacionModel {
        return try {
            val saldoValue = FormatoMonto.convertirADouble(saldo)
            if (saldoValue < 0 || saldoValue > 999_999_999.0) {
                ValidacionModel(false, "El saldo debe estar entre 0 y 999,999,999.00")
            } else {
                ValidacionModel(true)
            }
        } catch (e: NumberFormatException) {
            return ValidacionModel(false, "El saldo debe ser un número válido")
        }
    }

    /**
     * Valida la pantalla actual
     */
    fun validarPantalla(): Boolean {
        isNombreValido = validarNombre()
        isSaldoValido = validarSaldo()
        return isNombreValido && isSaldoValido.isValid
    }

    LaunchedEffect(Unit) {
        cuentasRepository.buscarCuentas(false, true) { resultadoCuentasPadres ->
            cuentasPadre = resultadoCuentasPadres ?: emptyList()
            Log.i("CuentasScreen", "Cuentas padre cargados: $resultadoCuentasPadres")

            if (idCuenta != 0L) {
                cuentasRepository.buscarCuentaPorId(idCuenta) { resultadoCuentaExistente ->
                    if (resultadoCuentaExistente != null) {
                        Log.i("CuentasScreen", "Activo encontrado: $resultadoCuentaExistente")
                        cuenta = resultadoCuentaExistente
                        nombre = resultadoCuentaExistente.nombre
                        descripcion = resultadoCuentaExistente.descripcion
                        saldo = FormatoMonto.formato(resultadoCuentaExistente.saldo)
                        cuentaSeleccionada =
                            cuentasPadre.find { it.id == resultadoCuentaExistente.padre?.id }

                        Log.i("CuentasScreen", "Cuenta padre encontrada: $cuentaSeleccionada")

                        descripcionBoton = "Actualizar"
                    }
                }

                cuentaPrincipalListaHabilitada = false
            }
        }
    }

    /**
     * Guarda la cuenta en la base de datos
     */
    fun guardarCuenta() {
        cuentasRepository.guardarCuenta(
            TransaccionCuentaRequestModel(
                nombre,
                descripcion,
                saldo.toDouble(),
                cuentaSeleccionada?.id
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

    /**
     * Actualiza la cuenta en la base de datos
     */
    fun actualizarCuenta() {
        cuentasRepository.actualizarCuenta(
            idCuenta, TransaccionCuentaRequestModel
                (nombre, descripcion, FormatoMonto.convertirADouble(saldo), cuentaSeleccionada?.id)
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
                            onClick = {
                                if (validarPantalla()) {
                                    if (idCuenta == 0L) {
                                        guardarCuenta()
                                    } else {
                                        actualizarCuenta()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = descripcionBoton
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CuentasDropDown(
                modifier = modifier,
                etiqueta = "Selecciona una Cuenta",
                cuentaPrincipalListaHabilitada,
                cuentaSeleccionada,
                cuentasPadre,
                onCuentaSeleccionada = {
                    cuentaSeleccionada = it
                }
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
                        )
                    }
                    Text(
                        text = "${nombre.length}/32",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            )

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
                }
            )

            OutlinedTextField(
                value = saldo,
                onValueChange = { newValue ->
                    val rawInput = newValue.replace("$", "").replace(",", "").replace(" ", "")
                    // Filtra la entrada para permitir solo números y un punto
                    val regex = Regex("^\\d{0,9}(\\.\\d{0,2})?\$")
                    if (regex.matches(rawInput)) {
                        saldo = "$rawInput"
                    }
                },
                label = { Text("Saldo") },
                leadingIcon = { Text("$") },
                isError = !isSaldoValido.isValid,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                supportingText = {
                    if (!isSaldoValido.isValid) {
                        Text(
                            text = isSaldoValido.mensaje!!,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                }
            )

        }
    }
}



