package com.blackdeath.planificadorapp.composables.portafolios

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
import com.blackdeath.planificadorapp.modelos.portafolios.PortafolioModel

/**
 * Composable que muestra un menú desplegable para seleccionar un portafolio
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortafoliosDropDown(
    modifier: Modifier,
    etiqueta: String,
    portafolios: List<PortafolioModel>,
    isHabilitado: Boolean = true,
    isError: Boolean = false,
    mensajeError: String = "",
    portafolioSeleccionado: PortafolioModel? = null,
    onPortafolioSeleccionado: (PortafolioModel) -> Unit
) {
    var isDropdownDesplegado by remember { mutableStateOf(false) }
    var portafolioSeleccionadoDropdown by remember { mutableStateOf(portafolioSeleccionado) }

    LaunchedEffect(portafolioSeleccionado, portafolios) {
        portafolioSeleccionadoDropdown = portafolios.find { it.id == portafolioSeleccionado?.id }
    }

    ExposedDropdownMenuBox(expanded = isDropdownDesplegado, onExpandedChange = {
        if (isHabilitado) {
            isDropdownDesplegado = !isDropdownDesplegado
        }
    }) {
        OutlinedTextField(value = portafolioSeleccionadoDropdown?.nombre ?: "",
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
            portafolios.forEach { portafolio ->
                DropdownMenuItem(text = { Text(portafolio.nombre) }, onClick = {
                    portafolioSeleccionadoDropdown = portafolio
                    isDropdownDesplegado = false
                    onPortafolioSeleccionado(portafolio)
                })
            }
        }
    }
}