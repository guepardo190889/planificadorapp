package com.example.planificadorapp.composables.cuentas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    isCuentaAgrupadoraSeleccionable: Boolean,
    cuentaSeleccionada: CuentaModel?,
    cuentas: List<CuentaModel>,
    onCuentaSeleccionada: (CuentaModel) -> Unit
) {
    var isDesplegadoDropdown by remember { mutableStateOf(false) }
    var cuentaSeleccionadaDropdown by remember { mutableStateOf(cuentaSeleccionada) }

    ExposedDropdownMenuBox(expanded = isDesplegadoDropdown, onExpandedChange = {
        if (isHabilitado) {
            isDesplegadoDropdown = !isDesplegadoDropdown
        }
    }) {
        OutlinedTextField(value = cuentaSeleccionadaDropdown?.nombre ?: "",
            onValueChange = { },
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = isHabilitado,
            readOnly = true,
            label = { Text(etiqueta, color = MaterialTheme.colorScheme.onSurface) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDesplegadoDropdown)
            })

        ExposedDropdownMenu(
            expanded = isDesplegadoDropdown,
            onDismissRequest = { isDesplegadoDropdown = false },
            modifier = modifier.fillMaxWidth()
        ) {
            cuentas.forEachIndexed { indice, cuenta ->
                val paddingStart = if (cuenta.isHija) 16.dp else 0.dp

                // Definir si la cuenta agrupadora está habilitada
                val isCuentaSeleccionable =
                    if (cuenta.agrupadora) isCuentaAgrupadoraSeleccionable else true

                // Ajustar el color de texto para cuentas agrupadoras no seleccionables
                val colorTexto = when {
                    cuenta.agrupadora && !isCuentaSeleccionable -> MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.4f
                    )

                    cuenta.agrupadora -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                DropdownMenuItem(modifier = Modifier
                    .padding(start = paddingStart)
                    .fillMaxWidth(),
                    text = {
                        Text(
                            cuenta.nombre,
                            style = MaterialTheme.typography.bodyMedium.copy(color = colorTexto)
                        )
                    },
                    enabled = isCuentaSeleccionable,
                    onClick = {
                        cuentaSeleccionadaDropdown = cuenta
                        isDesplegadoDropdown = false
                        onCuentaSeleccionada(cuenta)
                    })

                // Verifica si la siguiente cuenta es una cuenta agrupadora o si es la última cuenta de la lista
                val isUltimoElemento = indice == cuentas.size - 1
                val isProximaCuentaAgrupadora = !isUltimoElemento && cuentas[indice + 1].agrupadora
                val isProximaCuentaNoAgrupadaSinAgrupar =
                    !isUltimoElemento && !cuentas[indice + 1].agrupadora && cuentas[indice + 1].padre == null

                if (isUltimoElemento || isProximaCuentaAgrupadora || isProximaCuentaNoAgrupadaSinAgrupar) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}