package com.example.planificadorapp.screens.portafolios.guardado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.ActivoModel

/**
 * Composable que representa la pantalla del segundo paso en el guardado de un portafolio.
 * El segundo paso consiste en seleccionar los activos que se agregarán al portafolio.
 */
@Composable
fun GuardarPortafolioPasoDos(
    modifier: Modifier = Modifier,
    activos: List<ActivoModel>,
    activosSeleccionados: List<ActivoModel>,
    totalPorcentaje: Int,
    onAtrasClick: (List<ActivoModel>, Int) -> Unit,
    onSiguienteClick: (List<ActivoModel>, Int) -> Unit
) {
    var activosSeleccionadosPasoDos by remember {
        mutableStateOf(
            activosSeleccionados
        )
    }
    var mostrarDialogoActivos by remember { mutableStateOf(false) }
    var totalPorcentajePasoDos by remember { mutableStateOf(totalPorcentaje) }

    Scaffold(
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
                                onAtrasClick(activosSeleccionadosPasoDos, totalPorcentajePasoDos)
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Atrás"
                            )
                        }

                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                onSiguienteClick(activosSeleccionadosPasoDos, totalPorcentajePasoDos)
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = "Siguiente"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "Activos", style = MaterialTheme.typography.headlineMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            ActivosSeleccionadosList(
                activosSeleccionados = activosSeleccionadosPasoDos,
                onEliminarActivoSeleccionado = {
                    activosSeleccionadosPasoDos = activosSeleccionadosPasoDos - it
                    totalPorcentajePasoDos = sumarPorcentajes(activosSeleccionadosPasoDos)
                },
                onPorcentajeCambiado = {
                    totalPorcentajePasoDos = sumarPorcentajes(activosSeleccionadosPasoDos)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total: ${totalPorcentajePasoDos.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = {
                    mostrarDialogoActivos = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Agregar")
            }
        }

        if (mostrarDialogoActivos) {
            val activosDisponibles = activos.filterNot { activo ->
                activosSeleccionados.any { it.id == activo.id }
            }

            ActivosListaDialogo(activos = activosDisponibles,
                onActivoSeleccionado = { activoSeleccionado ->
                    activosSeleccionadosPasoDos = activosSeleccionadosPasoDos + activoSeleccionado
                    mostrarDialogoActivos = false
                },
                onDismissRequest = { mostrarDialogoActivos = false }
            )
        }
    }
}

/**
 * Composable que muestra una lista de activos seleccionados
 */
@Composable
fun ActivosSeleccionadosList(
    activosSeleccionados: List<ActivoModel>,
    modifier: Modifier = Modifier,
    onEliminarActivoSeleccionado: (ActivoModel) -> Unit,
    onPorcentajeCambiado: () -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {
        activosSeleccionados.forEach { activo ->
            ActivoSeleccionadoItem(
                activo,
                onEliminarActivoSeleccionado,
                onPorcentajeCambiado = onPorcentajeCambiado
            )
            HorizontalDivider()
        }
    }
}

/**
 * Composable que muestra un ítem de activo seleccionado
 */
@Composable
fun ActivoSeleccionadoItem(
    activoSeleccionado: ActivoModel,
    onEliminarActivoSeleccionado: (ActivoModel) -> Unit,
    onPorcentajeCambiado: () -> Unit
) {
    var sliderPosition by remember { mutableStateOf(activoSeleccionado.porcentaje) }

    ListItem(
        headlineContent = {
            Text(text = activoSeleccionado.nombre, style = MaterialTheme.typography.bodyLarge)
        },
        supportingContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        activoSeleccionado.porcentaje = it
                        onPorcentajeCambiado()
                    },
                    valueRange = 0f..100f
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .width(46.dp)
                        .height(46.dp),
                    value = sliderPosition.toInt().toString(),
                    onValueChange = {
                        val intValue = it.toIntOrNull()?.coerceIn(0, 100) ?: sliderPosition.toInt()
                        sliderPosition = intValue.toFloat()
                        activoSeleccionado.porcentaje = sliderPosition
                        onPorcentajeCambiado()
                    },
                    //label = { Text("%") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Text(modifier = Modifier.padding(2.dp), text = "%")
            }
        },
        trailingContent = {
            IconButton(onClick = {
                onEliminarActivoSeleccionado(activoSeleccionado)
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Activo")
            }
        }
    )
    HorizontalDivider()
}

fun sumarPorcentajes(activos: List<ActivoModel>): Int {
    return activos.sumOf { it.porcentaje.toDouble() }.toInt()
}