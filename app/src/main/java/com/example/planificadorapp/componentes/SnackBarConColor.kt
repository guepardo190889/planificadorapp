package com.example.planificadorapp.componentes

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SnackBarConColor(snackbarHostState: SnackbarHostState, tipo: String) {
    val backgroundColor = when (tipo) {
        "error" -> Color.Red
        "success" -> Color.Green
        "warning" -> Color(0xFFFFA500)
        else -> Color.Gray
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = backgroundColor,
                contentColor = Color.Black
            )
        }
    )
}