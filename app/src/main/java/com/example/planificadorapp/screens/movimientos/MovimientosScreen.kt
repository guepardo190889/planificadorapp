package com.example.planificadorapp.screens.movimientos

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.modelos.MovimientoModel
import com.example.planificadorapp.modelos.enumeradores.TipoMovimiento
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.MovimientosRepository
import com.example.planificadorapp.utilerias.FormatoFecha
import com.example.planificadorapp.utilerias.FormatoMonto
import com.example.planificadorapp.utilerias.enumeradores.FiltroMovimiento
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

/**
 * Composable que representa la pantalla de movimientos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimientosScreen(modifier: Modifier, navController: NavController) {
    val cuentasRepository = remember { CuentasRepository() }
    val movimientosRepository = remember { MovimientosRepository() }
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var movimientos by remember { mutableStateOf<List<MovimientoModel>>(emptyList()) }
    var isDropdownCuentaDesplegada by remember { mutableStateOf(false) }
    var cuentaSeleccionada by remember { mutableStateOf<CuentaModel?>(null) }
    var filtroSeleccionado by remember { mutableStateOf<FiltroMovimiento>(FiltroMovimiento.AL_DIA) }
    var isMostrarFiltros by remember { mutableStateOf(false) }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(Unit) {
        cuentasRepository.buscarCuentas(false, false) { resultadoCuentas ->
            Log.i("MovimientosScreen", "Cuentas cargadas: ${resultadoCuentas?.size}")
            cuentas = resultadoCuentas ?: emptyList()
        }
    }

    /**
     * Busca los movimientos de acuerdo a los filtros seleccionados
     */
    fun buscarMovimientos(): Unit {
        if (cuentaSeleccionada != null) {
            if (FiltroMovimiento.AL_DIA == filtroSeleccionado) {
                movimientosRepository.buscarMovimientos(
                    cuentaSeleccionada!!.id,
                    fechaInicio,
                    fechaFin,
                    true,
                    false,
                    false
                ) { resultadoMovimientos ->
                    Log.i(
                        "MovimientosScreen",
                        "Movimientos al día cargados: ${resultadoMovimientos?.size}"
                    )
                    movimientos = resultadoMovimientos ?: emptyList()
                }
            } else if (FiltroMovimiento.MES_ANTERIOR == filtroSeleccionado) {
                movimientosRepository.buscarMovimientos(
                    cuentaSeleccionada!!.id,
                    fechaInicio,
                    fechaFin,
                    false,
                    true,
                    false
                ) { resultadoMovimientos ->
                    Log.i(
                        "MovimientosScreen",
                        "Movimientos del mes anterior cargados: ${resultadoMovimientos?.size}"
                    )
                    movimientos = resultadoMovimientos ?: emptyList()
                }
            } else if (FiltroMovimiento.DOS_MESES_ATRAS == filtroSeleccionado) {
                movimientosRepository.buscarMovimientos(
                    cuentaSeleccionada!!.id,
                    fechaInicio,
                    fechaFin,
                    false,
                    false,
                    true
                ) { resultadoMovimientos ->
                    Log.i(
                        "MovimientosScreen",
                        "Movimientos dos meses atrás cargados: ${resultadoMovimientos?.size}"
                    )
                    movimientos = resultadoMovimientos ?: emptyList()
                }
            } else if (FiltroMovimiento.POR_FECHA == filtroSeleccionado) {
                movimientosRepository.buscarMovimientos(
                    cuentaSeleccionada!!.id,
                    fechaInicio,
                    fechaFin,
                    false,
                    false,
                    false
                ) { resultadoMovimientos ->
                    Log.i(
                        "MovimientosScreen",
                        "Movimientos cargados: ${resultadoMovimientos?.size}"
                    )
                    movimientos = resultadoMovimientos ?: emptyList()
                }
            }
        }
    }


    Scaffold(
        modifier = modifier.fillMaxWidth(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("movimientos/guardar") },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Guardar Movimiento")
            }
        },
        content = { paddingValues ->
            Column(
                modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = isDropdownCuentaDesplegada,
                    onExpandedChange = {
                        isDropdownCuentaDesplegada = it
                    }
                ) {
                    OutlinedTextField(
                        modifier = modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        value = cuentaSeleccionada?.nombre ?: "",
                        onValueChange = {},
                        label = { Text("Selecciona una Cuenta") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (isDropdownCuentaDesplegada) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                    )

                    ExposedDropdownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = isDropdownCuentaDesplegada,
                        onDismissRequest = { isDropdownCuentaDesplegada = false }
                    ) {
                        cuentas.forEach { cuenta ->
                            DropdownMenuItem(
                                text = { Text(text = cuenta.nombre) },
                                onClick = {
                                    cuentaSeleccionada = cuenta
                                    isDropdownCuentaDesplegada = false

                                    buscarMovimientos()
                                })
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isMostrarFiltros = !isMostrarFiltros }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Filtros")
                    Icon(
                        imageVector = if (isMostrarFiltros) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Mostrar filtros"
                    )
                }

                if (isMostrarFiltros) {
                    Column(modifier = modifier.padding(8.dp)) {
                        RadioButtonGroup(filtroSeleccionado) { nuevoFiltro ->
                            filtroSeleccionado = nuevoFiltro

                            if (FiltroMovimiento.POR_FECHA != filtroSeleccionado) {
                                // Limpiar los filtros de fecha si se cambia el filtro
                                fechaInicio = null
                                fechaFin = null

                                isMostrarFiltros = false
                                buscarMovimientos()
                            }
                        }

                        if (FiltroMovimiento.POR_FECHA == filtroSeleccionado) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                DatePickerInput(
                                    modifier.weight(1f),
                                    etiqueta = "Fecha Inicio",
                                    fecha = fechaInicio,
                                    onFechaSeleccionada = { fechaSeleccionada ->
                                        Log.i(
                                            "MovimientosScreen",
                                            "Fecha inicio seleccionada: $fechaSeleccionada"
                                        )
                                        fechaInicio = fechaSeleccionada
                                    },
                                    onDismiss = {
                                        Log.i("MovimientosScreen", "onDismiss")
                                    }
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                DatePickerInput(
                                    modifier.weight(1f),
                                    etiqueta = "Fecha Fin",
                                    fecha = fechaFin,
                                    onFechaSeleccionada = { fechaSeleccionada ->
                                        Log.i(
                                            "MovimientosScreen",
                                            "Fecha fin seleccionada: $fechaSeleccionada"
                                        )
                                        fechaFin = fechaSeleccionada
                                    },
                                    onDismiss = {
                                        Log.i("MovimientosScreen", "onDismiss")
                                    }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

                if (movimientos.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        text = "Sin movimientos"
                    )
                } else {
                    MovimientosList(
                        navController = navController,
                        movimientos = movimientos
                    )
                }
            }
        }
    )
}

/**
 * Composable que muestra un grupo de botones de radio para seleccionar un filtro
 */
@Composable
fun RadioButtonGroup(
    filtroSeleccionado: FiltroMovimiento,
    onFiltroSeleccionado: (FiltroMovimiento) -> Unit
) {
    Column {
        FiltroMovimiento.entries.forEach { filtro ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(
                    selected = filtro == filtroSeleccionado,
                    onClick = { onFiltroSeleccionado(filtro) }
                )
                Text(text = filtro.texto)
            }
        }
    }
}

/**
 * Composable que muestra un date picker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInput(
    modifier: Modifier = Modifier,
    etiqueta: String,
    fecha: LocalDate?,
    onFechaSeleccionada: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    var isMostrarDatePicker by remember { mutableStateOf(false) }
    val fechaTexto: String = if (fecha != null) FormatoFecha.formatoCortoISO8601(fecha) else "yyyy-MM-dd"

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (fecha != null) FormatoFecha.convertirLocalDateAMilisegundos(
            fecha
        ) else null
    )

    Box(
        modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = fechaTexto,
            onValueChange = {},
            label = { Text(etiqueta) },
            readOnly = true,
            maxLines = 1, //Limita a una línea para evitar debordes
            textStyle = TextStyle(fontSize = TextUnit.Unspecified),
            trailingIcon = {
                IconButton(onClick = { isMostrarDatePicker = !isMostrarDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecciona fecha"
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (isMostrarDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    isMostrarDatePicker = false
                    onDismiss()
                },
                confirmButton = {
                    TextButton(onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            onFechaSeleccionada(FormatoFecha.convertirMilisegundosALocalDate(selectedMillis))
                        }
                        isMostrarDatePicker = false // Cierra el diálogo
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        isMostrarDatePicker = false
                        onDismiss()
                    }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {
                            Text(
                                text = "Selecciona una fecha")
                        }
                    },
                    showModeToggle = false
                )
            }
        }
    }
}


/**
 * Composable que muestra la lista de movimientos
 */
@Composable
fun MovimientosList(
    modifier: Modifier = Modifier,
    navController: NavController,
    movimientos: List<MovimientoModel>,
) {
    LazyColumn(modifier.padding(16.dp)) {
        items(movimientos) { movimiento ->
            MovimientoItem(modifier, navController, movimiento)
            HorizontalDivider()
        }
    }
}

/**
 * Composable que muestra un ítem de movimiento
 */
@Composable
fun MovimientoItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    movimiento: MovimientoModel,
) {
    ListItem(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("movimientos/detalle/${movimiento.id}")
            },
        headlineContent = {
            Text(movimiento.descripcion)
        },
        supportingContent = {
            Text(
                text = FormatoFecha.formatoCortoConHora(movimiento.fecha)
            )
        },
        trailingContent = {
            if (TipoMovimiento.CARGO == movimiento.tipo) {
                Text(
                    text = "-${FormatoMonto.formato(movimiento.monto)}",
                    color = Color.Red
                )
            } else {
                Text(
                    text = FormatoMonto.formato(movimiento.monto),
                    color = Color.Green
                )
            }
        }
    )
}

