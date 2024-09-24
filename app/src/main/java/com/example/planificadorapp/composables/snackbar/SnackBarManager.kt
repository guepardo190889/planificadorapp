package com.example.planificadorapp.composables.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Clase que se encarga de mostrar un snackbar con un mensaje y un tipo
 */
class SnackBarManager(
    private val scope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState
) {
    var tipoSnackBar: SnackBarTipo = SnackBarTipo.SUCCESS
        private set

    /**
     * Muestra un snackbar con un mensaje y un tipo
     */
    fun mostrar(mensaje: String, tipo: SnackBarTipo, onDismiss: () -> Unit = {}) {
        scope.launch {
            tipoSnackBar = tipo
            snackbarHostState.showSnackbar(
                message = mensaje,
                duration = SnackbarDuration.Short
            )
            onDismiss()
        }
    }
}
