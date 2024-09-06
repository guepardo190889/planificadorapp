package com.example.planificadorapp.composables.cuentas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.planificadorapp.modelos.CuentaModel

/**
 * Composable que muestra un menú desplegable para seleccionar una cuenta
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CuentasDropDown(
    modifier: Modifier,
    etiqueta: String,
    isHabilitado: Boolean,
    cuentaSeleccionada: CuentaModel?,
    cuentas: List<CuentaModel>,
    onCuentaSeleccionada: (CuentaModel) -> Unit
) {
    var isDesplegadoDropdown by remember { mutableStateOf(false) }
    var cuentaSeleccionadaDropdown = cuentaSeleccionada

    ExposedDropdownMenuBox(
        expanded = isDesplegadoDropdown,
        onExpandedChange = {
            if (isHabilitado) {
                isDesplegadoDropdown = !isDesplegadoDropdown
            }
        }) {
        OutlinedTextField(
            value = cuentaSeleccionadaDropdown?.nombre ?: "",
            onValueChange = { },
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = isHabilitado,
            readOnly = true,
            label = { Text(etiqueta) },
            trailingIcon = {
                Icon(
                    imageVector = if (isDesplegadoDropdown) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }
        )

        ExposedDropdownMenu(
            expanded = isDesplegadoDropdown,
            onDismissRequest = { isDesplegadoDropdown = false },
            modifier = modifier.fillMaxWidth()
        ) {
            cuentas.forEach { cuenta ->
                DropdownMenuItem(
                    text = { Text(cuenta.nombre) },
                    onClick = {
                        cuentaSeleccionadaDropdown = cuenta
                        isDesplegadoDropdown = false
                        onCuentaSeleccionada(cuenta)
                    }
                )
            }
        }
    }
}