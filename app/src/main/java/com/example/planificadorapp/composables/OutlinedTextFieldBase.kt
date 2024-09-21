package com.example.planificadorapp.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    maxLength: Int,
    isError: Boolean? = null,
    errorMessage: String? = null,
    supportingText: String? = null,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            label = { Text(label) },
            isError = isError == true,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isError == true && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                supportingText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start
                    )
                }

                Text(
                    text = "${value.length}/$maxLength",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}