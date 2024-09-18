package com.example.planificadorapp.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

/**
 * Composable genérico de captura de texto con soporte para mensajes de error, texto adicional de soporte y conteo de caracteres
 */
@Composable
fun OutlinedTextFieldBase(
    value: String,
    label: String,
    maxLength: Int,
    isError: Boolean? = null,
    errorMessage: String? = null,
    supportingText: String? = null,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= maxLength) onValueChange(it) },
        label = { Text(label) },
        isError = isError ?: false,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(),
        modifier = Modifier.fillMaxWidth(),
        supportingText = {
            Column {
                if (isError == true && errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${value.length}/$maxLength",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End
                )

                supportingText?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = supportingText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    )
}