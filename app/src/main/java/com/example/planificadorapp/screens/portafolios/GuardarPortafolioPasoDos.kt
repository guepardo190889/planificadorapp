package com.example.planificadorapp.screens.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.configuracion.Ruta
import com.example.planificadorapp.modelos.ActivoModel
import com.example.planificadorapp.repositorios.ActivosRepository

@Composable
fun GuardarPortafolioPasoDos(navController: NavController) {
    val activosRepository = remember { ActivosRepository() }
    var activos by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var activosSeleccionados by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var mostrarDialogoActivos by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.i("GuardarPortafolioPasoDos", "Cargando activos...")
        activosRepository.buscarActivos { result ->
            Log.i("GuardarPortafolioPasoDos", "Activos encontrados: $result")
            activos = result ?: emptyList()
        }
    }

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
                            onClick = { navController.navigate(Ruta.PORTAFOLIOS_GUARDAR_PASO_TRES.ruta) }
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
                activosSeleccionados = activosSeleccionados,
                onEliminarActivoSeleccionado = {
                    activosSeleccionados = activosSeleccionados - it
                }
            )
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
            val activosDisponibles = activos.filterNot {
                activo ->
                activosSeleccionados.any { it.id == activo.id }
            }

            ActivosListaDialogo(activos = activosDisponibles,
                onActivoSeleccionado = { activoSeleccionado ->
                    activosSeleccionados = activosSeleccionados + activoSeleccionado
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
    onEliminarActivoSeleccionado: (ActivoModel) -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {
        activosSeleccionados.forEach { activo ->
            ActivoSeleccionadoItem(activo, onEliminarActivoSeleccionado)
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
    onEliminarActivoSeleccionado: (ActivoModel) -> Unit
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
                    modifier = Modifier.weight(1f),
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        activoSeleccionado.porcentaje = it
                    },
                    valueRange = 0f..100f
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    modifier = Modifier.width(80.dp),
                    value = sliderPosition.toInt().toString(),
                    onValueChange = {
                        val intValue = it.toIntOrNull()?.coerceIn(0, 100) ?: sliderPosition.toInt()
                        sliderPosition = intValue.toFloat()
                        activoSeleccionado.porcentaje = sliderPosition
                    },
                    label = { Text("%") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
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
