package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.activos.SeleccionarActivoDialogo
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel

/**
 * Composable que muestra la distribución de los activos en un portafolio
 */
@Composable
fun PortafolioDistribucionActivos(
    modifier: Modifier = Modifier,
    activos: List<ActivoModel>,
    composiciones: List<GuardarComposicionModel>,
    onAgregarComposicion: (GuardarComposicionModel) -> Unit,
    onEliminarComposicion: (GuardarComposicionModel) -> Unit,
    onPorcentajeCambiado: (GuardarComposicionModel, Int) -> Unit,
    onAtrasClick: () -> Unit,
    onSiguienteClick: () -> Unit
) {
    var totalPorcentaje by remember { mutableIntStateOf(0) }
    var isComposicionesValidas by remember { mutableStateOf(true) }
    var mensajeErrorComposiciones by remember { mutableStateOf("") }
    var mostrarDialogoActivos by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    var isMostrarFlechaArriba by remember { mutableStateOf(false) }
    var isMostrarFlechaAbajo by remember { mutableStateOf(false) }

    val firstVisibleItemIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }

    val firstVisibleItemOffset by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset
        }
    }

    val lastVisibleItem by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()
        }
    }

    val isLastItemVisible by remember {
        derivedStateOf {
            lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    val isLastItemCompletelyVisible by remember {
        derivedStateOf {
            val lastItem = lastVisibleItem
            lastItem != null &&
                    (lastItem.size + lastItem.offset) <= listState.layoutInfo.viewportEndOffset &&
                    listState.layoutInfo.totalItemsCount > 0 &&
                    lastItem.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(composiciones, listState, firstVisibleItemIndex, firstVisibleItemOffset) {
        totalPorcentaje = composiciones.sumOf { it.porcentaje.toInt() }

        Log.i("PortafolioDistribucionActivos", "Número de composiciones: ${composiciones.size}")

        if (composiciones.isNotEmpty()) {
            // Verificar si se debe mostrar la flecha hacia arriba
            isMostrarFlechaArriba = firstVisibleItemIndex > 0 || firstVisibleItemOffset > 0
            Log.i("PortafolioDistribucionActivos", "isMostrarFlechaArriba: $isMostrarFlechaArriba")

            // Verificar si se debe mostrar la flecha hacia abajo
            isMostrarFlechaAbajo = !(isLastItemVisible && isLastItemCompletelyVisible)

            // Debug para verificar el comportamiento al eliminar elementos
            Log.i(
                "PortafolioDistribucionActivos",
                "isLastItemVisible: $isLastItemVisible, isLastItemCompletelyVisible: $isLastItemCompletelyVisible"
            )
            Log.i("PortafolioDistribucionActivos", "isMostrarFlechaAbajo: $isMostrarFlechaAbajo")
        } else {
            // Si no hay composiciones, no mostrar flechas
            isMostrarFlechaArriba = false
            isMostrarFlechaAbajo = false
            Log.i("PortafolioDistribucionActivos", "No hay composiciones. Flechas ocultas.")
        }

        // Nueva verificación explícita después de un cambio en el tamaño de las composiciones
        if (composiciones.size > 0) {
            // Asegurarse de que las flechas se actualicen correctamente al eliminar o agregar elementos
            val isLastCompositionVisible = lastVisibleItem?.index == composiciones.size - 1
            val isFullyVisible = isLastCompositionVisible && isLastItemCompletelyVisible

            if (isLastCompositionVisible && !isFullyVisible) {
                isMostrarFlechaAbajo = true
            } else {
                isMostrarFlechaAbajo = false
            }
            Log.i("PortafolioDistribucionActivos", "isFullyVisible: $isFullyVisible, isMostrarFlechaAbajo: $isMostrarFlechaAbajo")
        }
    }

    /**
     * Función que valida si las composiciones son válidas
     */
    fun validarComposiciones() {
        isComposicionesValidas = true

        when {
            composiciones.isEmpty() -> {
                isComposicionesValidas = false
                mensajeErrorComposiciones = "El portafolio debe tener al menos un activo"
            }

            composiciones.any { it.porcentaje < 1 } -> {
                isComposicionesValidas = false
                mensajeErrorComposiciones = "Todos los porcentajes deben ser mayores a 0"
            }

            totalPorcentaje != 100 -> {
                isComposicionesValidas = false
                mensajeErrorComposiciones = "La suma de los porcentajes debe ser igual a 100"
            }
        }
    }

    Scaffold(bottomBar = {
        BarraNavegacionInferior(onAtrasClick = onAtrasClick, onSiguienteClick = {
            validarComposiciones()

            if (isComposicionesValidas) {
                onSiguienteClick()
            }
        })
    }, content = { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            EncabezadoPortafolio(titulo = "Distribución de activos")

            Box(modifier = Modifier.weight(1f, false)) {
                if (isMostrarFlechaArriba) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Desplazar arriba",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .padding(16.dp),
                    state = listState
                ) {
                    items(composiciones) { composicion ->
                        ComposicionItem(
                            composicion = composicion,
                            onEliminarComposicion = onEliminarComposicion,
                            onPorcentajeCambiado = onPorcentajeCambiado
                        )
                        HorizontalDivider()
                    }
                }

                if (isMostrarFlechaAbajo) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Desplazar abajo",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isComposicionesValidas) {
                    Log.i(
                        "PortafolioDistribucionActivos",
                        "mensajeErrorComposiciones: $mensajeErrorComposiciones"
                    )
                    Text(
                        text = mensajeErrorComposiciones,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                SeccionPorcentajeTotalDistribucionActivos(totalPorcentaje)

                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { mostrarDialogoActivos = true }) {
                    Text("Agregar activo")
                }
            }

            if (mostrarDialogoActivos) {
                val activosDisponibles = activos.filterNot { activo ->
                    composiciones.any { it.activo == activo }
                }

                SeleccionarActivoDialogo(activosDisponibles,
                    onActivoSeleccionado = { activoSeleccionado ->
                        onAgregarComposicion(GuardarComposicionModel(activoSeleccionado))
                        mostrarDialogoActivos = false
                    },
                    onDismissRequest = { mostrarDialogoActivos = false })
            }
        }
    })
}

/**
 * Composable que muestra un activo con su porcentaje y opciones para eliminarlo
 */
@Composable
fun ComposicionItem(
    composicion: GuardarComposicionModel,
    onEliminarComposicion: (GuardarComposicionModel) -> Unit,
    onPorcentajeCambiado: (GuardarComposicionModel, Int) -> Unit,
) {
    var posicionSlider by remember { mutableFloatStateOf(composicion.porcentaje) }

    ListItem(headlineContent = {
        Text(
            text = composicion.activo.nombre, style = MaterialTheme.typography.bodyLarge
        )
    }, supportingContent = {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                modifier = Modifier.fillMaxWidth(0.7f), value = posicionSlider, onValueChange = {
                    posicionSlider = it
                    onPorcentajeCambiado(composicion, it.toInt())
                }, valueRange = 0f..100f
            )
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .width(56.dp)
                    .height(46.dp),
                value = posicionSlider.toInt().toString(),
                onValueChange = {
                    val nuevoPorcentaje =
                        it.toIntOrNull()?.coerceIn(0, 100) ?: posicionSlider.toInt()
                    posicionSlider = nuevoPorcentaje.toFloat()
                    onPorcentajeCambiado(composicion, nuevoPorcentaje)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Text(modifier = Modifier.padding(2.dp), text = "%")
        }
    }, trailingContent = {
        IconButton(onClick = {
            onEliminarComposicion(composicion)
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar Activo")
        }
    })
    HorizontalDivider()
}

/**
 * Composable que muestra el total de porcentaje de distribución de activos de un portafolio
 */
@Composable
fun SeccionPorcentajeTotalDistribucionActivos(totalPorcentaje: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Total: ${totalPorcentaje}%",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}