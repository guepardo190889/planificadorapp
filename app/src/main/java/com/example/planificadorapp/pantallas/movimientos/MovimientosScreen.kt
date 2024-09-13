package com.example.planificadorapp.pantallas.movimientos

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.R
import com.example.planificadorapp.composables.DatePickerInput
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
    val cuentasRepository = CuentasRepository()
    val movimientosRepository = MovimientosRepository()
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var movimientos by remember { mutableStateOf<List<MovimientoModel>>(emptyList()) }
    var cuentaSeleccionada by remember { mutableStateOf<CuentaModel?>(null) }
    var filtroSeleccionado by remember { mutableStateOf(FiltroMovimiento.AL_DIA) }
    var isMostrarFiltros by remember { mutableStateOf(false) }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }

    val scrollState = rememberLazyListState()
    val isFabVisible by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }

    LaunchedEffect(Unit) {
        cuentasRepository.buscarCuentas(
            excluirCuentasAsociadas = false,
            incluirSoloCuentasNoAgrupadorasSinAgrupar = false
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

            Log.i("MovimientosScreen", "Movimientos cargados: $movimientos")
            Log.i("MovimientosScreen", "Movimientos cargados: ${movimientos.size}")
        }
    }

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        floatingActionButton = {
            AnimatedVisibility (
                visible = isFabVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate("movimientos/guardar") },
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Guardar Movimiento")
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                CuentasDropDown(
                    modifier = modifier,
                    etiqueta = "Selecciona una cuenta",
                    isHabilitado = true,
                    isCuentaAgrupadoraSeleccionable = false,
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
                    FiltrosMovimientos(
                        modifier = modifier,
                        filtroSeleccionado = filtroSeleccionado,
                        onFiltroSeleccionado = { filtroSeleccionado = it },
                        fechaInicio = fechaInicio,
                        onFechaInicioSeleccionada = { fechaInicio = it },
                        fechaFin = fechaFin,
                        onFechaFinSeleccionada = { fechaFin = it },
                        onAplicarClick = {
                            if (filtroSeleccionado != FiltroMovimiento.POR_FECHA) {
                                fechaInicio = null
                                fechaFin = null
                            }
                            isMostrarFiltros = false
                            buscarMovimientos()
                        }
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                if (movimientos.isEmpty()) {
                    Text(
                        modifier = modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        text = "Sin movimientos",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .weight(1f),
                        state = scrollState
                    ) {
                        items(movimientos) { movimiento ->
                            MovimientoItem(modifier, navController, movimiento)
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }
        }
    )
}

/**
 * Composable que muestra los filtros de movimientos
 */
@Composable
fun FiltrosMovimientos(
    modifier: Modifier,
    filtroSeleccionado: FiltroMovimiento,
    onFiltroSeleccionado: (FiltroMovimiento) -> Unit,
    fechaInicio: LocalDate?,
    onFechaInicioSeleccionada: (LocalDate?) -> Unit,
    fechaFin: LocalDate?,
    onFechaFinSeleccionada: (LocalDate?) -> Unit,
    onAplicarClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        RadioButtonGroup(filtroSeleccionado, onFiltroSeleccionado)

        if (FiltroMovimiento.POR_FECHA == filtroSeleccionado) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerInput(
                    modifier = Modifier.weight(1f),
                    etiqueta = "Fecha Inicio",
                    fecha = fechaInicio,
                    onFechaSeleccionada = onFechaInicioSeleccionada,
                    onDismiss = {}
                )

                Spacer(modifier = Modifier.width(16.dp))

                DatePickerInput(
                    modifier = Modifier.weight(1f),
                    etiqueta = "Fecha Fin",
                    fecha = fechaFin,
                    onFechaSeleccionada = onFechaFinSeleccionada,
                    onDismiss = {}
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onAplicarClick,
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
            Text(movimiento.concepto ?: "", color = MaterialTheme.colorScheme.onSurface)
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
                }"
                else FormatoMonto.formato(movimiento.monto),
                color = color
            )
        }
    )
}