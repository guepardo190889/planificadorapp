package com.blackdeath.planificadorapp.composables.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

/**
 * Composable que muestra un Snackbar con un mensaje y un tipo de Snackbar.
 */
@Composable
fun SnackBarBase(
    snackbarHostState: SnackbarHostState,
    snackBarManager: SnackBarManager
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            val type = snackBarManager.tipoSnackBar

            val backgroundColor = when (type) {
                SnackBarTipo.ERROR -> MaterialTheme.colorScheme.error
                SnackBarTipo.SUCCESS -> MaterialTheme.colorScheme.primary
                SnackBarTipo.WARNING -> MaterialTheme.colorScheme.secondary
            }

            val contentColor = when (type) {
                SnackBarTipo.ERROR -> MaterialTheme.colorScheme.onError
                SnackBarTipo.SUCCESS -> MaterialTheme.colorScheme.onPrimary
                SnackBarTipo.WARNING -> MaterialTheme.colorScheme.secondary
            }

            Snackbar(
                snackbarData = snackbarData,
                containerColor = backgroundColor,
                contentColor = contentColor
            )
        }
    )
}