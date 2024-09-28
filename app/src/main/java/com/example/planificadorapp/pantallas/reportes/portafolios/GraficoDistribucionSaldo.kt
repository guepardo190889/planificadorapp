package com.example.planificadorapp.pantallas.reportes.portafolios

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Composable que representa el gráfico de distribución de saldos de un portafolio
 */
@Composable
fun GraficoDistribucionSaldo(modifier: Modifier){
    Text(
        text = "Grafico GraficoDistribucionSaldo",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}