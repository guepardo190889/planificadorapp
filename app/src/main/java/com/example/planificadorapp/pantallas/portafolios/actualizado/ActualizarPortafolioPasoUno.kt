package com.example.planificadorapp.pantallas.portafolios.actualizado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.pantallas.portafolios.PortafolioDatosGenerales
import com.example.planificadorapp.utilerias.validadores.PortafolioValidador

/**
 * Composable que representa la pantalla del primer paso en la actualización de un portafolio.
 * El primer paso consiste en editar los datos generales del portafolio
 */
@Composable
fun ActualizarPortafolioPasoUno(
    modifier: Modifier = Modifier,
    nombre: String,
    descripcion: String,
    onSiguienteClick: (String, String) -> Unit
) {
    val nombreEditado = remember { mutableStateOf(nombre) }
    val descripcionEditada = remember { mutableStateOf(descripcion) }
    val isNombreValido = remember { mutableStateOf(true) }

    /**
     * Valida la pantalla actual antes de navegar a la siguiente
     */
    fun validarPantalla(): Boolean {
        isNombreValido.value = PortafolioValidador.validarNombre(nombreEditado.value)
        return isNombreValido.value
    }

    Scaffold(bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingActionButton(modifier = Modifier.padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = {
                            if (validarPantalla()) {
                                onSiguienteClick(nombreEditado.value, descripcionEditada.value)
                            }
                        }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = "Siguiente"
                        )
                    }
                }
            })
    }, content = { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            PortafolioDatosGenerales(nombre = nombreEditado,
                descripcion = descripcionEditada,
                isNombreValido = isNombreValido,
                onNombreChange = { nuevoNombre ->
                    if (nuevoNombre.length <= 20) {
                        isNombreValido.value = true

                        nombreEditado.value = nuevoNombre
                    }
                },
                onDescripcionChange = { nuevaDescripcion ->
                    if (nuevaDescripcion.length <= 256) {
                        descripcionEditada.value = nuevaDescripcion
                    }
                })
        }
    })
}