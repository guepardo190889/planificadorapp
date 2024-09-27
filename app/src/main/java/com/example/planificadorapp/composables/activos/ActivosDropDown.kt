package com.example.planificadorapp.composables.activos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.planificadorapp.modelos.activos.ActivoModel

/**
 * Composable que muestra un menú desplegable para seleccionar un activo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivosDropDonw(
    modifier:Modifier,
    activoSelecionado:ActivoModel,
    activos:List<ActivoModel>,
    onActivoSeleccionado:(ActivoModel)->Unit
) {
    var isDesplegadoDropdown by remember { mutableStateOf(false) }
    var activoSeleccionadoDropdown by remember { mutableStateOf(activoSelecionado) }
/*
    ExposedDropdownMenuBox(
        expanded = isDesplegadoDropdown,
        onExpandedChange = {
            if (activoPrincipalListaHabilitada) { //No se permite modificar el activo principal de un activo existente
                isDesplegadoDropdown = !isDesplegadoDropdown
            }
        }) {
        OutlinedTextField(
            value = activoSeleccionadoDropdown?.nombre ?: "",
            onValueChange = { },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = activoPrincipalListaHabilitada,
            readOnly = true,
            label = {
                Text(
                    "Selecciona un Activo Principal",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDesplegadoDropdown)
            },
            isError = !isActivoSeleccionadoValido,
            supportingText = {
                if (!isActivoSeleccionadoValido) {
                    Text(
                        text = "El activo principal es requerido",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors()
        )

        ExposedDropdownMenu(
            expanded = isDesplegadoDropdown,
            onDismissRequest = { isDesplegadoDropdown = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            activos.forEach { activo ->
                DropdownMenuItem(
                    text = { Text(activo.nombre) },
                    onClick = {
                        activoSeleccionado = activo
                        activoPrincipalListaDesplegada = false
                        isActivoSeleccionadoValido = true
                    }
                )
            }
        }
    }*/
}