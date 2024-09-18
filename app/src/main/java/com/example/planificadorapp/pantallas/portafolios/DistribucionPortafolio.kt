package com.example.planificadorapp.pantallas.portafolios

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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel

/**
 * Composable que muestra la distribución de los activos en un portafolio
 */
@Composable
fun DistribucionPortafolio(
    modifier: Modifier = Modifier,
    activos: List<ActivoModel>,
    composiciones: List<GuardarComposicionModel>,
    totalPorcentaje: Int,
    onComposicionesChange: (List<GuardarComposicionModel>, Int) -> Unit
) {
    var composicionesActuales by remember { mutableStateOf(composiciones) }
    var totalPorcentajeActual by remember { mutableIntStateOf(totalPorcentaje) }
    var mostrarDialogoActivos by remember { mutableStateOf(false) }

    Column (
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "Composición de activos",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        ComposicionesList(
            composiciones = composicionesActuales,
            onEliminarComposicion = {
                composicionesActuales = composicionesActuales - it
                totalPorcentajeActual = composicionesActuales.sumOf { it.porcentaje.toInt() }
                onComposicionesChange(composicionesActuales, totalPorcentajeActual)
            },
            onPorcentajeCambiado = { composicion, nuevoPorcentaje ->
                val nuevasComposiciones = composicionesActuales.map {
                    if (it == composicion) {
                        it.copy(porcentaje = nuevoPorcentaje)
                    } else {
                        it
                    }
                }
                composicionesActuales = nuevasComposiciones
                totalPorcentajeActual = nuevasComposiciones.sumOf { it.porcentaje.toInt() }
                onComposicionesChange(nuevasComposiciones, totalPorcentajeActual)
            }
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Total: ${totalPorcentajeActual}%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Button (
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { mostrarDialogoActivos = true }
        ) {
            Text("Agregar activo")
        }

        if (mostrarDialogoActivos) {
            val activosDisponibles = activos.filterNot { activo ->
                composicionesActuales.any { it.activo == activo }
            }

            SeleccionarActivoDialogo(
                activosDisponibles,
                onActivoSeleccionado = { activoSeleccionado ->
                    composicionesActuales = composicionesActuales + GuardarComposicionModel(
                        activoSeleccionado
                    )
                    mostrarDialogoActivos = false
                    onComposicionesChange(composicionesActuales, totalPorcentajeActual)
                },
                onDismissRequest = { mostrarDialogoActivos = false }
            )
        }
    }
}

/**
 * Composable que muestra una lista de activos seleccionados con opciones de eliminación
 * y modificación de los porcentajes.
 */
@Composable
fun ComposicionesList(
    composiciones: List<GuardarComposicionModel>,
    modifier: Modifier = Modifier,
    onEliminarComposicion: (GuardarComposicionModel) -> Unit,
    onPorcentajeCambiado: (GuardarComposicionModel, Float) -> Unit
) {
    LazyColumn (modifier = modifier.padding(16.dp)) {
        items(composiciones) { composicion ->
            ComposicionItem(
                composicion = composicion,
                onEliminarComposicion = onEliminarComposicion,
                onPorcentajeCambiado = { nuevoPorcentaje ->
                    onPorcentajeCambiado(composicion, nuevoPorcentaje)
                }
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
    onPorcentajeCambiado: (Float) -> Unit
) {
    var posicionSlider by remember { mutableFloatStateOf(composicion.porcentaje) }

    ListItem(
        headlineContent = {
            Text(
                text = composicion.activo.nombre,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    value = posicionSlider,
                    onValueChange = {
                        posicionSlider = it
                        onPorcentajeCambiado(it)
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
                        val intValue = it.toIntOrNull()?.coerceIn(0, 100) ?: posicionSlider.toInt()
                        posicionSlider = intValue.toFloat()
                        onPorcentajeCambiado(posicionSlider)
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Text(modifier = Modifier.padding(2.dp), text = "%")
            }
        },
        trailingContent = {
            IconButton(onClick = {
                onEliminarComposicion(composicion)
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Activo")
            }
        }
    )
    HorizontalDivider()
}