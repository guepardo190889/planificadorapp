package com.example.planificadorapp.screens.portafolios.guardado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.modelos.ActivoModel

@Composable
fun GuardarPortafolioPasoTres(
    activosSeleccionados: List<ActivoModel>,
    onAtrasClick: () -> Unit,
    onSiguienteClick: () -> Unit
) {
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
                                onAtrasClick()
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = "Siguiente"
                            )
                        }

                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                onSiguienteClick()
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
            Text(text = "Cuentas", style = MaterialTheme.typography.headlineMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            activosSeleccionados.forEach { activo ->
                ActivoCard(activo)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun ActivoCard(activo: ActivoModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(
                text = activo.nombre,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Button(
                onClick = {
                    //mostrar diálogo para listar cuentas disponibles
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Agregar")
            }
        }
    }
}