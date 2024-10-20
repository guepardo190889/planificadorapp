package com.blackdeath.planificadorapp.composables

import androidx.compose.foundation.layout.Arrangement
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
fun TextoEtiquetado(
    etiqueta: String,
    texto: String,
    estiloEtiqueta: TextStyle = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
    estiloTexto: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = etiqueta, style = estiloEtiqueta, modifier = Modifier.width(100.dp)
        )
        Text(
            text = texto, style = estiloTexto, modifier = Modifier.weight(1f), maxLines = 3
        )
    }
}