package com.example.planificadorapp.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * Composable que muestra una etiqueta y a su lado un texto
 */
@Composable
fun TextoConEtiqueta(etiqueta: String, texto: String, styleLabel: String, styleBody: String) {

    val textStyleLabel: TextStyle = when (styleLabel) {
        "large" -> MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.onSurfaceVariant)
        "medium" -> MaterialTheme.typography.labelMedium.copy(MaterialTheme.colorScheme.onSurfaceVariant)
        "small" -> MaterialTheme.typography.labelSmall.copy(MaterialTheme.colorScheme.onSurfaceVariant)
        else -> MaterialTheme.typography.labelMedium.copy(MaterialTheme.colorScheme.onSurfaceVariant)
    }

    val textStyleBody: TextStyle = when (styleBody) {
        "large" -> MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
        "medium" -> MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
        "small" -> MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
        else -> MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .width(92.dp)
                .align(Alignment.Top),
            text = etiqueta,
            style = textStyleLabel
        )
        Text(
            modifier = Modifier
                .align(Alignment.Bottom),
            text = texto,
            maxLines = 3,
            style = textStyleBody
        )
    }
}