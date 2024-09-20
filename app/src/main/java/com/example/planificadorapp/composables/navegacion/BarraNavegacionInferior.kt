package com.example.planificadorapp.composables.navegacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Composable que muestra la barra inferior de navegación
 */
@Composable
fun BarraNavegacionInferior(
    modifier: Modifier = Modifier,
    isTransaccionGuardar: Boolean? = true,
    onAtrasClick: (() -> Unit)? = null,
    onSiguienteClick: (() -> Unit)? = null,
    onTransaccionClick: (() -> Unit)? = null
) {
    BottomAppBar(modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = when {
                    onAtrasClick != null && onSiguienteClick != null -> Arrangement.SpaceBetween
                    onAtrasClick != null && onTransaccionClick != null -> Arrangement.SpaceBetween
                    onAtrasClick != null -> Arrangement.Start
                    onSiguienteClick != null -> Arrangement.End
                    else -> Arrangement.Center
                }
            ) {
                if (onAtrasClick != null) {
                    FloatingActionButton(
                        onClick = onAtrasClick,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }

                if (onSiguienteClick != null) {
                    FloatingActionButton(
                        onClick = onSiguienteClick,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = "Siguiente"
                        )
                    }
                }

                if (onTransaccionClick != null) {
                    FloatingActionButton(
                        onClick = onTransaccionClick,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = if (isTransaccionGuardar == true) "Guardar" else "Actualizar"
                        )
                    }
                }
            }
        })
}