package com.example.planificadorapp.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.utilerias.FormatoFecha
import java.time.LocalDate

/**
 * Composable que muestra un date picker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInput(
    modifier: Modifier = Modifier,
    etiqueta: String,
    fecha: LocalDate?,
    onFechaSeleccionada: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    var isMostrarDatePicker by remember { mutableStateOf(false) }

    // Estado del DatePicker
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fecha?.let { FormatoFecha.convertirLocalDateAMilisegundos(it) }
    )

    // Función que maneja la selección de la fecha
    fun onDateSelected() {
        val selectedMillis = datePickerState.selectedDateMillis
        if (selectedMillis != null) {
            onFechaSeleccionada(FormatoFecha.convertirMilisegundosALocalDate(selectedMillis))
        }
        isMostrarDatePicker = false
    }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            value = fecha?.let { FormatoFecha.formatoCortoISO8601(it) } ?: "yyyy-MM-dd",
            onValueChange = {},
            label = { Text(etiqueta, color = MaterialTheme.colorScheme.onSurface) },
            readOnly = true,
            maxLines = 1,
            textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
            trailingIcon = {
                IconButton(onClick = { isMostrarDatePicker = !isMostrarDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecciona fecha",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors()
        )

        if (isMostrarDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    isMostrarDatePicker = false
                    onDismiss()
                },
                confirmButton = {
                    TextButton(onClick = { onDateSelected() }) {
                        Text("Aceptar", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        isMostrarDatePicker = false
                        onDismiss()
                    }) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "Selecciona una fecha")
                        }
                    },
                    showModeToggle = false
                )
            }
        }
    }
}