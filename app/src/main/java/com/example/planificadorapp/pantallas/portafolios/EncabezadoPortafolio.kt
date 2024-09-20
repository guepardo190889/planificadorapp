package com.example.planificadorapp.pantallas.portafolios

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable que muestra el encabezado de las diferentes pantallas de guardado o actualización de un portafolio
 */
@Composable
fun EncabezadoPortafolio(modifier: Modifier = Modifier, titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(bottom = 4.dp)
    )
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.outline
    )
}