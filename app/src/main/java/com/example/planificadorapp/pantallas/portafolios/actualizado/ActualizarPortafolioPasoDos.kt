package com.example.planificadorapp.pantallas.portafolios.actualizado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.pantallas.portafolios.DistribucionPortafolio

/**
 * Composable que representa la pantalla del segundo paso en la actualización de un portafolio.
 * El segundo paso consiste en seleccionar los activos que se agregarán al portafolio.
 */
@Composable
fun ActualizarPortafolioPasoDos(
    modifier: Modifier = Modifier,
    activos: List<ActivoModel>,
    composiciones: List<GuardarComposicionModel>,
    totalPorcentaje: Int,
    onAtrasClick: (List<GuardarComposicionModel>, Int) -> Unit,
    onSiguienteClick: (List<GuardarComposicionModel>, Int) -> Unit
) {


    Scaffold(bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingActionButton(
                        onClick = { onAtrasClick(composiciones, totalPorcentaje) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Atrás")
                    }

                    FloatingActionButton(
                        onClick = { onSiguienteClick(composiciones, totalPorcentaje) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = "Siguiente"
                        )
                    }
                }
            })
    }, content = { paddingValues ->
        DistribucionPortafolio(modifier = Modifier.padding(paddingValues),
            activos = activos,
            composiciones = composiciones,
            totalPorcentaje = totalPorcentaje,
            onComposicionesChange = { nuevasComposiciones, nuevoTotal ->
                onSiguienteClick(nuevasComposiciones, nuevoTotal)
            })
    })
}