package com.example.planificadorapp.composables.cuentas

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.cuentas.CuentaModel

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
            cuentas.forEachIndexed { indice, cuenta ->
                if (cuenta.isPadre) {
                    // Cuenta padre
                    DropdownMenuItem(
                        text = {
                            Text(
                                cuenta.nombre,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                            )
                        },
                        onClick = {
                            cuentaSeleccionadaDropdown = cuenta
                            isDesplegadoDropdown = false
                            onCuentaSeleccionada(cuenta)
                        }
                    )
                } else {
                    // Cuenta hija
                    DropdownMenuItem(
                        text = {
                            Row {
                                Spacer(modifier = Modifier.width(16.dp)) // Añade indentación para las cuentas hijas
                                Text(cuenta.nombre)
                            }
                        },
                        onClick = {
                            cuentaSeleccionadaDropdown = cuenta
                            isDesplegadoDropdown = false
                            onCuentaSeleccionada(cuenta)
                        }
                    )
                }

                // Verifica si la siguiente cuenta es una cuenta padre o si es la última cuenta de la lista
                val isUltimoElemento = indice == cuentas.size - 1
                val isProximoPadre = !isUltimoElemento && cuentas[indice + 1].isPadre

                if (isUltimoElemento || isProximoPadre) {
                    HorizontalDivider()
                }
            }
        }
    }
}