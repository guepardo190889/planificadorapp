package com.example.planificadorapp.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable que muestra el encabezado de una pantalla
 */
@Composable
fun EncabezadoScreen(
    modifier: Modifier = Modifier,
    titulo: String,
    descripcion: String? = null
) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(bottom = 4.dp)
    )

    if (!descripcion.isNullOrEmpty()) {
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(bottom = 4.dp)
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.outline
    )
}