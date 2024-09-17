package com.example.planificadorapp.pantallas.portafolios.guardado

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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
 * Composable que representa la pantalla del primer paso en el guardado de un portafolio.
 * El primer paso consiste en ingresar los datos generales del portafolio.
 */
@Composable
fun GuardarPortafolioPasoUno(
    modifier: Modifier = Modifier,
    nombre: String,
    descripcion: String,
    onSiguienteClick: (String, String) -> Unit
) {
    var nombrePasoUno by remember { mutableStateOf(nombre) }
    var descripcionPasoUno by remember { mutableStateOf(descripcion) }

    var isNombreValido by remember { mutableStateOf(true) }

    /**
     * Valida la pantalla actual
     */
    fun validarPantalla(): Boolean {
        isNombreValido = PortafolioValidador.validarNombre(nombrePasoUno)

        return isNombreValido
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                if (validarPantalla()) {
                                    onSiguienteClick(nombrePasoUno, descripcionPasoUno)
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
        },
        content = { paddingValues ->
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
                    value = nombrePasoUno,
                    onValueChange = {
                        isNombreValido = PortafolioValidador.validarNombre(it)

                        if (it.length <= 20) {
                            nombrePasoUno = it
                        }
                    },
                    label = { Text("Nombre") },
                    isError = !isNombreValido,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        if (!isNombreValido) {
                            Text(
                                text = "El nombre es requerido",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "${nombrePasoUno.length}/20",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End
                        )
                    }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = descripcionPasoUno,
                    onValueChange = {
                        if (it.length <= 256) {
                            descripcionPasoUno = it
                        }
                    },
                    label = { Text("Descripción") },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(),
                    supportingText = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "${descripcionPasoUno.length}/256",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End
                        )
                    }
                )
            }
        }
    )
}
