package com.example.planificadorapp.screens.cuentas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.repositorios.CuentaRepository
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DetalleCuentaScreen(navController: NavController, cuentaId: Long) {
    var cuenta by remember { mutableStateOf<CuentaModel?>(null) }
    val cuentaRepository = remember { CuentaRepository() }

    LaunchedEffect(cuentaId) {
        cuentaRepository.buscarCuentaPorId(cuentaId) { result ->
            cuenta = result
        }
    }

    cuenta?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = it.nombre,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = formatCurrency(it.saldo),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = it.descripcion,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Última actualización: ${
                            it.fechaActualizacion?.format(
                                DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))
                            )
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = { /* Acción para actualizar la cuenta */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Actualizar")
                    }
                }
            }
            // Aquí puedes añadir los Sliders y Cards para Tareas y Comentarios
            /*Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tareas (2)",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Añade tu Slider aquí
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Comentarios (3)",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Añade tu Slider aquí
                }
            }*/
        }
    }
}
