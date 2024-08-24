package com.example.planificadorapp.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmacionSimpleDialog(
    texto: String,
    onDismissRequest: () -> Unit,
    onConfirmar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        },
        text = {
            Text(text = texto, style = MaterialTheme.typography.bodyMedium)
        }
    )
}