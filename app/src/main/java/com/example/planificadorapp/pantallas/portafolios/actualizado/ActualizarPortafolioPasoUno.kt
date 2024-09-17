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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    var nombreEditado by remember { mutableStateOf(nombre) }
    var descripcionEditada by remember { mutableStateOf(descripcion) }

    var isNombreValido by remember { mutableStateOf(true) }

    fun validarPantalla(): Boolean {
        isNombreValido = PortafolioValidador.validarNombre(nombreEditado)
        return isNombreValido
    }

    Scaffold (
        bottomBar = {
            BottomAppBar (
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                content = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton (
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                if (validarPantalla()) {
                                    onSiguienteClick(nombreEditado, descripcionEditada)
                                }
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
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Generales",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outline
            )

            OutlinedTextField(
                value = nombreEditado,
                onValueChange = {
                    isNombreValido = PortafolioValidador.validarNombre(it)

                    if (it.length <= 20) {
                        nombreEditado = it
                    }
                },
                label = { Text("Nombre") },
                isError = !isNombreValido,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (!isNombreValido) {
                        Text(text = "El nombre es requerido", color = MaterialTheme.colorScheme.error)
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${nombreEditado.length}/20",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End
                    )
                }
            )

            OutlinedTextField(
                value = descripcionEditada,
                onValueChange = { descripcionEditada = it },
                label = { Text("Descripción") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${descripcionEditada.length}/256",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End
                    )
                }
            )
        }
    }
}