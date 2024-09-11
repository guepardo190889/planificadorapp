package com.example.planificadorapp.pantallas.movimientos

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
import com.example.planificadorapp.composables.DatePickerInput
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.composables.cuentas.CuentasDropDown
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.movimientos.TransaccionMovimientoRequestModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.MovimientosRepository
import com.example.planificadorapp.utilerias.FormatoMonto
import com.example.planificadorapp.utilerias.enumeradores.TipoMovimiento
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Composable que representa la pantalla de transacción (guardado/actualización) de un movimiento
 */
@Composable
fun TransaccionMovimientosScreen(
    modifier: Modifier,
    navController: NavController,
    idMovimiento: Long
) {
    val movimientosRepository = remember { MovimientosRepository() }
    val cuentasRepository = remember { CuentasRepository() }

    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var subcuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }

    var cuentaSeleccionada by remember { mutableStateOf<CuentaModel?>(null) }
    var subcuentaSeleccionada by remember { mutableStateOf<CuentaModel?>(null) }
    var monto by remember { mutableStateOf("") }
    var concepto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf<LocalDate?>(null) }
    var tipoMovimientoSeleccionado by remember { mutableStateOf(TipoMovimiento.CARGO) }

    var descripcionBoton by remember { mutableStateOf("Guardar") }

    var isMontoValido by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fecha = LocalDate.now()

        cuentasRepository.buscarCuentas(
            excluirCuentasAsociadas = false,
            incluirSoloCuentasPadre = true
        ) { resultadoCuentas ->
            cuentas = resultadoCuentas ?: emptyList()
            Log.i("TransaccionMovimientosScreen", "Cuentas cargadas: $resultadoCuentas")
        }

        if (idMovimiento != 0L) {
            descripcionBoton = "Actualizar"
            //TODO Buscar movimiento
        }
    }

    /**
     * Busca las subcuentas de la cuenta seleccionada
     */
    fun buscarSubcuentas() {
        cuentasRepository.buscarSubcuentas(cuentaSeleccionada!!.id) {
            subcuentas = it ?: emptyList()
        }
    }

    /**
     * Valida si el monto es válido
     */
    fun validarMonto(): Boolean {
        return try {
            val montoValue = FormatoMonto.convertirADouble(monto)
            montoValue > 0
        } catch (e: NumberFormatException) {
            return false
        }
    }

    /**
     * Valida la pantalla actual
     */
    fun validarPantalla(): Boolean {
        isMontoValido = validarMonto()
        return isMontoValido
    }

    /**
     * Guarda el movimiento en la base de datos
     */
    fun guardarMovimiento() {
        val idCuentaGuardar =
            if (subcuentaSeleccionada != null) subcuentaSeleccionada!!.id else cuentaSeleccionada!!.id

        movimientosRepository.guardarMovimiento(
            TransaccionMovimientoRequestModel(
                monto.toDouble(),
                concepto,
                descripcion,
                fecha!!,
                idCuentaGuardar,
                tipoMovimientoSeleccionado.name
            )
        ) { movimientoGuardado ->
            if (movimientoGuardado != null) {
                snackbarMessage = "Movimiento guardado exitosamente"
                snackbarType = "success"
            } else {
                snackbarMessage = "Error al guardar el movimiento"
            }

            coroutineScope.launch {
                snackbarHostState.showSnackbar(snackbarMessage)

                if (movimientoGuardado != null) {
                    navController.navigate("movimientos")
                }
            }
        }
    }

    /**
     * Actualiza el movimiento en la base de datos
     */
    fun actualizarMovimiento() {

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
                                    if (idMovimiento == 0L) {
                                        guardarMovimiento()
                                    } else {
                                        actualizarMovimiento()
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
        },
        content = { paddingValues ->
            Column(
                modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TipoMovimiento.entries.forEach { tipo ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = tipo == tipoMovimientoSeleccionado,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    tipoMovimientoSeleccionado = tipo
                                }
                            )
                            Text(text = tipo.name, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                CuentasDropDown(
                    modifier = modifier,
                    etiqueta = "Selecciona una Cuenta",
                    true,
                    cuentaSeleccionada,
                    cuentas,
                    onCuentaSeleccionada = {
                        subcuentas = emptyList()
                        subcuentaSeleccionada = null

                        cuentaSeleccionada = it
                        buscarSubcuentas()
                    }
                )

                if (subcuentas.isNotEmpty()) {
                    CuentasDropDown(
                        modifier = modifier,
                        etiqueta = "Selecciona una Subcuenta",
                        true,
                        subcuentaSeleccionada,
                        subcuentas,
                        onCuentaSeleccionada = {
                            subcuentaSeleccionada = it
                        }
                    )
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = monto,
                    onValueChange = { nuevoMonto ->
                        val regex = Regex("^\\d{0,9}(\\.\\d{0,2})?\$")
                        if (regex.matches(nuevoMonto)) {
                            monto = nuevoMonto
                        }
                    },
                    label = { Text("Monto", color = MaterialTheme.colorScheme.onSurface) },
                    leadingIcon = { Text("$", color = MaterialTheme.colorScheme.onSurface) },
                    isError = !isMontoValido,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = {
                        if (!isMontoValido) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "El monto es requerido",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                )

                DatePickerInput (
                    modifier,
                    etiqueta = "Fecha",
                    fecha = fecha,
                    onFechaSeleccionada = { fechaSeleccionada ->
                        fecha = fechaSeleccionada
                    },
                    onDismiss = {

                    }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = concepto,
                    onValueChange = {
                        if (it.length <= 64) {
                            concepto = it
                        }
                    },
                    label = { Text("Concepto", color = MaterialTheme.colorScheme.onSurface) },
                    singleLine = false,
                    maxLines = 2,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(),
                    supportingText = {
                        Text(
                            text = "${concepto.length}/64",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = descripcion,
                    onValueChange = {
                        if (it.length <= 256) {
                            descripcion = it
                        }
                    },
                    label = { Text("Descripción", color = MaterialTheme.colorScheme.onSurface) },
                    singleLine = false,
                    maxLines = 3,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(),
                    supportingText = {
                        Text(
                            text = "${descripcion.length}/256",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                )
            }
        }
    )
}