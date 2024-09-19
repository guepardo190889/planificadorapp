package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
    val totalPorcentaje by remember {
        derivedStateOf { composiciones.sumOf { it.porcentaje.toInt() } }
    }
    var isComposicionesValidas by remember { mutableStateOf(true) }
    var mensajeErrorComposiciones by remember { mutableStateOf("") }
    var mostrarDialogoActivos by remember { mutableStateOf(false) }

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

    Scaffold(
        bottomBar = {
            BarraNavegacionInferior(
                onAtrasClick = onAtrasClick,
                onSiguienteClick = {
                    validarComposiciones()

                    if(isComposicionesValidas){
                        onSiguienteClick()
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                EncabezadoPortafolio("Composición de activos")

                ComposicionesList(
                    modifier = modifier,
                    composiciones = composiciones,
                    onEliminarComposicion = onEliminarComposicion,
                    onPorcentajeCambiado = onPorcentajeCambiado
                )

                if(!isComposicionesValidas) {
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
 * Composable que muestra una lista de activos seleccionados con opciones de eliminación
 * y modificación de los porcentajes.
 */
@Composable
fun ComposicionesList(
    modifier: Modifier,
    composiciones: List<GuardarComposicionModel>,
    onEliminarComposicion: (GuardarComposicionModel) -> Unit,
    onPorcentajeCambiado: (GuardarComposicionModel, Int) -> Unit
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(composiciones) { composicion ->
            ComposicionItem(
                composicion = composicion,
                onEliminarComposicion = onEliminarComposicion,
                onPorcentajeCambiado = onPorcentajeCambiado
            )
            HorizontalDivider()
        }
    }
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
                modifier = Modifier.fillMaxWidth(0.7f),
                value = posicionSlider,
                onValueChange = {
                    posicionSlider = it
                    onPorcentajeCambiado(composicion, it.toInt())
                },
                valueRange = 0f..100f
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Total: ${totalPorcentaje}%",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}