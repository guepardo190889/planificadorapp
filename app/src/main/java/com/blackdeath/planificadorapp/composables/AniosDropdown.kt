package com.blackdeath.planificadorapp.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.time.Year

/**
 * Composable que muestra un menú desplegable para seleccionar un año
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AniosDropDown(
    modifier: Modifier = Modifier,
    etiqueta: String = "Selecciona el Año",
    onAnioSeleccionado: (Int) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val anios = (2023..Year.now().value).toList() // Lista de años desde 2023 hasta el año actual
    var anioSeleccionado by remember { mutableIntStateOf(Year.now().value) } // Selecciona por defecto el año actual

    ExposedDropdownMenuBox(expanded = isDropdownExpanded, onExpandedChange = {
        isDropdownExpanded = !isDropdownExpanded
    }) {
        OutlinedTextField(
            value = anioSeleccionado.toString(),
            onValueChange = { },
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = { Text(etiqueta) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        ExposedDropdownMenu(expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }) {
            anios.forEach { anio ->
                DropdownMenuItem(text = { Text(anio.toString()) }, onClick = {
                    anioSeleccionado = anio
                    isDropdownExpanded = false
                    onAnioSeleccionado(anio) // Devolver el año seleccionado
                })
            }
        }
    }
}