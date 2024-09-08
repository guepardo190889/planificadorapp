package com.example.planificadorapp.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

/**
 * Composable que muestra un Snackbar con un color personalizado
 */
@Composable
fun SnackBarConColor(snackbarHostState: SnackbarHostState, tipo: String) {
    val backgroundColor = when (tipo) {
        "error" -> MaterialTheme.colorScheme.error
        "success" -> MaterialTheme.colorScheme.primary
        "warning" -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (tipo) {
        "error" -> MaterialTheme.colorScheme.onError
        "success" -> MaterialTheme.colorScheme.onPrimary
        "warning" -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = backgroundColor,
                contentColor = contentColor
            )
        }
    )
}