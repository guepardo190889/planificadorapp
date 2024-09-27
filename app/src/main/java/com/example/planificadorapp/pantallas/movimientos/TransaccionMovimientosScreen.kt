package com.example.planificadorapp.pantallas.movimientos

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.DatePickerInput
import com.example.planificadorapp.composables.cuentas.CuentasDropDown
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.composables.snackbar.SnackBarBase
import com.example.planificadorapp.composables.snackbar.SnackBarManager
import com.example.planificadorapp.composables.snackbar.SnackBarTipo
import com.example.planificadorapp.composables.textfield.DineroTextField
import com.example.planificadorapp.composables.textfield.OutlinedTextFieldBase
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.movimientos.MovimientoModel
import com.example.planificadorapp.modelos.movimientos.TransaccionMovimientoRequestModel
import com.example.planificadorapp.navegacion.Ruta
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.MovimientosRepository
import com.example.planificadorapp.utilerias.enumeradores.TipoMovimiento
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Composable que representa la pantalla de transacción (guardado/actualización) de un movimiento
 */
@Composable
fun TransaccionMovimientosScreen(
    modifier: Modifier, navController: NavController, idMovimiento: Long
) {
    val movimientosRepository = MovimientosRepository()
    val cuentasRepository = CuentasRepository()

    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var cuentaSeleccionada by remember { mutableStateOf<CuentaModel?>(null) }
    var movimiento by remember { mutableStateOf<MovimientoModel?>(null) }

    var monto by remember { mutableStateOf("") }
    var concepto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var tipoMovimientoSeleccionado by remember { mutableStateOf(TipoMovimiento.CARGO) }

    var isTransaccionGuardar by remember { mutableStateOf(true) }
    var descripcionBoton by remember { mutableStateOf("Guardar") }

    var isMontoValido by remember { mutableStateOf(true) }
    var isCuentaSeleccionadaValida by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarManager = remember { SnackBarManager(coroutineScope, snackbarHostState) }

    val conceptoFocusRequester = remember { FocusRequester() }
    val descripcionFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var isTransaccionando by remember { mutableStateOf(false) }

    /**
     * Valida si el monto es válido
     */
    fun validarMonto(): Boolean {
        return try {
            if (monto.isNotEmpty()) {
                BigDecimal(monto.replace(",", "")) >= BigDecimal.ZERO
            } else {
                false
            }
        } catch (e: NumberFormatException) {
            false
        }
    }

    /**
     * Valida si la cuenta seleccionada es válida
     */
    fun validarCuentaSeleccionada():Boolean {
        return cuentaSeleccionada != null
    }

    /**
     * Valida la pantalla actual
     */
    fun validarPantalla(): Boolean {
        isMontoValido = validarMonto()
        isCuentaSeleccionadaValida = validarCuentaSeleccionada()
        return isMontoValido && isCuentaSeleccionadaValida
    }

    /**
     * Guarda o actualiza el movimiento
     */
    fun transaccionarMovimiento() {
        isTransaccionando = true

        val montoTransaccion = if (monto.isNotEmpty()) {
            BigDecimal(monto.replace(",", ""))
        } else {
            BigDecimal.ZERO
        }

        val transaccionModel = TransaccionMovimientoRequestModel(
            montoTransaccion,
            concepto,
            descripcion,
            fecha!!,
            cuentaSeleccionada!!.id,
            tipoMovimientoSeleccionado.name
        )

        if (isTransaccionGuardar) {
            movimientosRepository.guardarMovimiento(transaccionModel) { movimientoGuardado ->
                if (movimientoGuardado != null) {
                    snackBarManager.mostrar(
                        "Movimiento guardado exitosamente", SnackBarTipo.SUCCESS
                    ) {
                        isTransaccionando = false
                        navController.navigate(Ruta.MOVIMIENTOS.ruta)
                    }
                } else {
                    snackBarManager.mostrar("Error al guardar el movimiento", SnackBarTipo.ERROR) {
                        isTransaccionando = false
                    }
                }
            }
        } else {
            movimientosRepository.actualizarMovimiento(
                idMovimiento, transaccionModel
            ) { movimientoActualizado ->
                if (movimientoActualizado != null) {
                    snackBarManager.mostrar(
                        "Movimiento actualizado exitosamente", SnackBarTipo.SUCCESS
                    ) {
                        isTransaccionando = false
                        navController.navigate(Ruta.MOVIMIENTOS.ruta)
                    }
                } else {
                    snackBarManager.mostrar(
                        "Error al actualizar el movimiento", SnackBarTipo.ERROR
                    ) {
                        isTransaccionando = false
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        cuentasRepository.buscarCuentas(
            excluirCuentasAsociadas = false, incluirSoloCuentasNoAgrupadorasSinAgrupar = false
        ) { resultadoCuentas ->
            cuentas = resultadoCuentas ?: emptyList()
            Log.i("TransaccionMovimientosScreen", "Cuentas cargadas: ${cuentas.size}")

            if (idMovimiento != 0L) {
                movimientosRepository.buscarMovimientoPorId(idMovimiento) { resultadoMovimiento ->
                    if (resultadoMovimiento != null) {
                        Log.i(
                            "TransaccionMovimientosScreen",
                            "Movimiento encontrado: $resultadoMovimiento"
                        )

                        movimiento = resultadoMovimiento
                        monto = resultadoMovimiento.monto.toString()
                        concepto = resultadoMovimiento.concepto ?: ""
                        descripcion = resultadoMovimiento.descripcion ?: ""
                        fecha = resultadoMovimiento.fecha
                        tipoMovimientoSeleccionado = resultadoMovimiento.tipo

                        cuentaSeleccionada = cuentas.find { it.id == resultadoMovimiento.idCuenta }
                    }
                }

                isTransaccionGuardar = false
                descripcionBoton = "Actualizar"
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxWidth(), snackbarHost = {
        SnackBarBase(
            snackbarHostState = snackbarHostState, snackBarManager = snackBarManager
        )
    }, bottomBar = {
        BarraNavegacionInferior(isTransaccionGuardar = isTransaccionGuardar, onTransaccionClick = {
            if (validarPantalla()) {
                transaccionarMovimiento()
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
                TipoMovimientoRadioButtonGroup(tipoMovimientoSeleccionado,
                    onSelect = { tipoMovimientoSeleccionado = it })

                CuentasDropDown(modifier = modifier,
                    etiqueta = "Selecciona una Cuenta",
                    isHabilitado = isTransaccionGuardar,
                    isCuentaAgrupadoraSeleccionable = false,
                    cuentas =cuentas,
                    isError = !isCuentaSeleccionadaValida,
                    mensajeError = "La cuenta es requerida",
                    cuentaSeleccionada = cuentaSeleccionada,
                    onCuentaSeleccionada = {
                        isCuentaSeleccionadaValida = true
                        cuentaSeleccionada = it
                    })

                DineroTextField(modifier = modifier.fillMaxWidth(),
                    etiqueta = "Monto",
                    mensajeError = "El monto es requerido",
                    monto = monto,
                    isError = !isMontoValido,
                    onSaldoChange = {
                        monto = it
                    },
                    onNextAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })

                DatePickerInput(modifier,
                    etiqueta = "Fecha",
                    fecha = fecha,
                    onFechaSeleccionada = { fechaSeleccionada ->
                        fecha = fechaSeleccionada
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    onDismiss = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })

                OutlinedTextFieldBase(modifier = Modifier.fillMaxWidth(),
                    value = concepto,
                    label = "Concepto",
                    maxLength = 64,
                    singleLine = false,
                    maxLines = 2,
                    focusRequester = conceptoFocusRequester,
                    onValueChange = {
                        concepto = it
                    },
                    onNextAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })

                OutlinedTextFieldBase(modifier = Modifier.fillMaxWidth(),
                    value = descripcion,
                    label = "Descripción",
                    maxLength = 256,
                    singleLine = false,
                    maxLines = 3,
                    focusRequester = descripcionFocusRequester,
                    onValueChange = {
                        descripcion = it
                    })
            }

            // Si está guardando, mostramos un indicador de carga que bloquea toda la pantalla
            if (isTransaccionando) {
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

/**
 * Composable que representa un grupo de botones de tipo de movimiento
 */
@Composable
fun TipoMovimientoRadioButtonGroup(
    tipoSeleccionado: TipoMovimiento, onSelect: (TipoMovimiento) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TipoMovimiento.entries.filter { it == TipoMovimiento.CARGO || it == TipoMovimiento.ABONO }
            .forEach { tipo ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tipo == tipoSeleccionado,
                        onClick = { onSelect(tipo) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = tipo.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
    }
}