package com.example.planificadorapp.screens.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.modelos.ActivoModel
import com.example.planificadorapp.repositorios.ActivosRepository

@Composable
fun GuardarPortafolioPasoDos(navController: NavController) {
    val activosRepository = remember { ActivosRepository() }
    var activos by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var activosSeleccionados by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }

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
                content = {
                    Column {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            navController.navigate("/portafolios/guardar/pasotres")
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
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
                activosSeleccionados = activosSeleccionados
            )
            Button(onClick = { /* Agregar un nuevo activo */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Agregar")
            }
        }
    }
}

/**
 * Composable que muestra una lista de activos seleccionados
 */
@Composable
fun ActivosSeleccionadosList(
    activosSeleccionados: List<ActivoModel>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        activosSeleccionados.forEach { activo ->
            ActivoSeleccionadoItem(activo)
            HorizontalDivider()
        }
    }
}

/**
 * Composable que muestra un ítem de activo seleccionado
 */
@Composable
fun ActivoSeleccionadoItem(activoSeleccionado: ActivoModel) {
    ListItem(
        headlineContent = { Text(text = activoSeleccionado.nombre) },
        supportingContent = {
            Column {
                Slider(
                    value = activoSeleccionado.porcentaje,
                    onValueChange = { newValue ->
                        //TODO: Actualizar el valor del activo seleccionado
                    },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = activoSeleccionado.porcentaje.toString(),
                    onValueChange = { newValue ->
                        //TODO: Actualizar el valor del activo seleccionado
                    },
                    label = { Text("%") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        trailingContent = {
            IconButton(onClick = {
                //TODO: Eliminar el activo seleccionado
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Activo")
            }
        }
    )
    HorizontalDivider()
}
