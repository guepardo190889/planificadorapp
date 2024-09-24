package com.example.planificadorapp.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
    singleLine: Boolean = true,
    maxLines: Int = 1,
    isError: Boolean? = null,
    errorMessage: String? = null,
    supportingText: String? = null,
    focusRequester: FocusRequester? = null,
    onValueChange: (String) -> Unit,
    onNextAction: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .then(
                if (focusRequester != null) Modifier.focusRequester(focusRequester)
                else Modifier
            ),
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            label = { Text(label) },
            isError = isError == true,
            singleLine = singleLine,
            maxLines = maxLines,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = if (onNextAction != null) ImeAction.Next else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                if (onNextAction == null) {
                    focusManager.clearFocus()
                }
            }, onNext = {
                if (onNextAction != null) {
                    onNextAction()
                }
            })
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

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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