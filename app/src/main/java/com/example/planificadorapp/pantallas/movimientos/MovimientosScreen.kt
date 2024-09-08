package com.example.planificadorapp.pantallas.movimientos

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.R
import com.example.planificadorapp.composables.cuentas.CuentasDropDown
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.movimientos.MovimientoModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.MovimientosRepository
import com.example.planificadorapp.utilerias.FormatoFecha
import com.example.planificadorapp.utilerias.FormatoMonto
import com.example.planificadorapp.utilerias.enumeradores.FiltroMovimiento
import com.example.planificadorapp.utilerias.enumeradores.TipoMovimiento
import java.time.LocalDate

/**
 * Composable que representa la pantalla de movimientos
 */
@Composable
fun MovimientosScreen(modifier: Modifier, navController: NavController) {
    val cuentasRepository = remember { CuentasRepository() }
    val movimientosRepository = remember { MovimientosRepository() }
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var movimientos by remember { mutableStateOf<List<MovimientoModel>>(emptyList()) }
    var cuentaSeleccionada by remember { mutableStateOf<CuentaModel?>(null) }
    var filtroSeleccionado by remember { mutableStateOf(FiltroMovimiento.AL_DIA) }
    var isMostrarFiltros by remember { mutableStateOf(false) }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(Unit) {
        cuentasRepository.buscarCuentas(
            excluirCuentasAsociadas = false,
            incluirSoloCuentasPadre = false
        ) { resultadoCuentas ->
            cuentas = resultadoCuentas ?: emptyList()
            Log.i("MovimientosScreen", "Cuentas cargadas: $cuentas.size")
        }
    }

    /**
     * Busca los movimientos de acuerdo a los filtros seleccionados
     */
    fun buscarMovimientos() {
        if (cuentaSeleccionada != null) {
            if (FiltroMovimiento.AL_DIA == filtroSeleccionado) {
                movimientosRepository.buscarMovimientos(
                    cuentaSeleccionada!!.id,
                    fechaInicio,
                    fechaFin,
                    isAlDia = true,
                    isMesAnterior = false,
                    isDosMesesAtras = false
                ) { resultadoMovimientos ->
                    movimientos = resultadoMovimientos ?: emptyList()
                }
            } else if (FiltroMovimiento.MES_ANTERIOR == filtroSeleccionado) {
                movimientosRepository.buscarMovimientos(
                    cuentaSeleccionada!!.id,
                    fechaInicio,
                    fechaFin,
                    isAlDia = false,
                    isMesAnterior = true,
                    isDosMesesAtras = false
                ) { resultadoMovimientos ->
                    movimientos = resultadoMovimientos ?: emptyList()
                }
            } else if (FiltroMovimiento.DOS_MESES_ATRAS == filtroSeleccionado) {
                movimientosRepository.buscarMovimientos(
                    cuentaSeleccionada!!.id,
                    fechaInicio,
                    fechaFin,
                    isAlDia = false,
                    isMesAnterior = false,
                    isDosMesesAtras = true
                ) { resultadoMovimientos ->
                    movimientos = resultadoMovimientos ?: emptyList()
                }
            } else if (FiltroMovimiento.POR_FECHA == filtroSeleccionado) {
                movimientosRepository.buscarMovimientos(
                    cuentaSeleccionada!!.id,
                    fechaInicio,
                    fechaFin,
                    isAlDia = false,
                    isMesAnterior = false,
                    isDosMesesAtras = false
                ) { resultadoMovimientos ->
                    movimientos = resultadoMovimientos ?: emptyList()
                }
            }

            Log.i("MovimientosScreen", "Movimientos cargados: ${movimientos.size}")
        }
    }

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("movimientos/guardar") },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Guardar Movimiento")
            }
        },
        content = {
            Column(
                modifier
                    .padding(it)
                    .padding(16.dp)
            ) {
                CuentasDropDown(
                    modifier = modifier,
                    etiqueta = "Selecciona una cuenta",
                    isHabilitado = true,
                    cuentaSeleccionada = cuentaSeleccionada,
                    cuentas = cuentas,
                    onCuentaSeleccionada = { cuenta ->
                        cuentaSeleccionada = cuenta
                        buscarMovimientos()
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isMostrarFiltros = !isMostrarFiltros }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Filtros", color = MaterialTheme.colorScheme.onBackground)
                    Icon(
                        imageVector = if (isMostrarFiltros) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Mostrar filtros",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (isMostrarFiltros) {
                    Column(modifier = modifier.padding(8.dp)) {
                        RadioButtonGroup(filtroSeleccionado) { nuevoFiltro ->
                            filtroSeleccionado = nuevoFiltro
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

                        Row(
                            modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    // Limpiar los filtros de fecha si se cambia el filtro
                                    if (FiltroMovimiento.POR_FECHA != filtroSeleccionado) {
                                        fechaInicio = null
                                        fechaFin = null
                                    }

                                    isMostrarFiltros = false

                                    buscarMovimientos()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text(text = "Aplicar")
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                if (movimientos.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        text = "Sin movimientos",
                        color = MaterialTheme.colorScheme.onBackground
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = filtro == filtroSeleccionado,
                    onClick = { onFiltroSeleccionado(filtro) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(text = filtro.texto, color = MaterialTheme.colorScheme.onSurface)
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
    val fechaTexto: String =
        if (fecha != null) FormatoFecha.formatoCortoISO8601(fecha) else "yyyy-MM-dd"

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (fecha != null) FormatoFecha.convertirLocalDateAMilisegundos(
            fecha
        ) else null
    )

    Box(
        modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            value = fechaTexto,
            onValueChange = {},
            label = { Text(etiqueta, color = MaterialTheme.colorScheme.onSurface) },
            readOnly = true,
            maxLines = 1, //Limita a una línea para evitar debordes
            textStyle = TextStyle(fontSize = TextUnit.Unspecified),
            trailingIcon = {
                IconButton(onClick = { isMostrarDatePicker = !isMostrarDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecciona fecha",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
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
                            onFechaSeleccionada(
                                FormatoFecha.convertirMilisegundosALocalDate(
                                    selectedMillis
                                )
                            )
                        }
                        isMostrarDatePicker = false // Cierra el diálogo
                    }) {
                        Text("Aceptar", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        isMostrarDatePicker = false
                        onDismiss()
                    }) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Selecciona una fecha"
                            )
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
    LazyColumn {
        items(movimientos) { movimiento ->
            MovimientoItem(modifier, navController, movimiento)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
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
    movimiento: MovimientoModel
) {
    ListItem(
        modifier = modifier
            .clickable {
                navController.navigate("movimientos/detalle/${movimiento.id}")
            },
        headlineContent = {
            Text(movimiento.descripcion, color = MaterialTheme.colorScheme.onSurface)
        },
        supportingContent = {
            Text(
                text = FormatoFecha.formatoCorto(movimiento.fecha),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.outline_swap_horiz_24),
                contentDescription = "Movimiento",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            val color = if (TipoMovimiento.CARGO == movimiento.tipo) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.secondary
            }

            Text(
                text = if (TipoMovimiento.CARGO == movimiento.tipo) "-${
                    FormatoMonto.formato(
                        movimiento.monto
                    )
                }" else FormatoMonto.formato(movimiento.monto),
                color = color
            )
        }
    )
}