package com.example.planificadorapp.composables.activos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.planificadorapp.modelos.activos.ActivoModel

/**
 * Composable que muestra un menú desplegable para seleccionar un activo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivosDropDonw(
    modifier: Modifier,
    etiqueta: String,
    activos: List<ActivoModel>,
    isHabilitado: Boolean = true,
    isError: Boolean = false,
    mensajeError: String = "",
    activoSeleccionado: ActivoModel?,
    onActivoSeleccionado: (ActivoModel) -> Unit
) {
    var isDropdownDesplegado by remember { mutableStateOf(false) }
    var activoSeleccionadoDropdown by remember { mutableStateOf(activoSeleccionado) }

    LaunchedEffect(activoSeleccionado, activos) {
        activoSeleccionadoDropdown = activos.find { it.id == activoSeleccionado?.id }
    }

    ExposedDropdownMenuBox(expanded = isDropdownDesplegado, onExpandedChange = {
        if (isHabilitado) {
            isDropdownDesplegado = !isDropdownDesplegado
        }
    }) {
        OutlinedTextField(value = activoSeleccionadoDropdown?.nombre ?: "",
            onValueChange = { },
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = isHabilitado,
            readOnly = true,
            isError = isError,
            label = { Text(etiqueta, color = MaterialTheme.colorScheme.onSurface) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownDesplegado)
            },
            supportingText = {
                if (isError) {
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            })

        ExposedDropdownMenu(
            expanded = isDropdownDesplegado,
            onDismissRequest = { isDropdownDesplegado = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            activos.forEach { activo ->
                DropdownMenuItem(text = { Text(activo.nombre) }, onClick = {
                    activoSeleccionadoDropdown = activo
                    isDropdownDesplegado = false
                    onActivoSeleccionado(activo)
                })
            }
        }
    }
}