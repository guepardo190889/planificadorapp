package com.example.planificadorapp.composables.cuentas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.cuentas.CuentaModel

/**
 * Composable que muestra un diálogo para seleccionar una cuenta
 */
@Composable
fun SeleccionarCuentaDialogo(
    cuentas: List<CuentaModel>,
    onCuentaSeleccionada: (CuentaModel) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = onDismissRequest) {
            Text(
                text = "Cerrar", color = MaterialTheme.colorScheme.primary
            )
        }
    }, text = {
        Column {
            Text(
                text = "Selecciona una Cuenta",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            cuentas.forEachIndexed { indice, cuenta ->
                val colorTexto = if (cuenta.isAgrupadora) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) // Color gris para deshabilitar
                } else {
                    MaterialTheme.colorScheme.onSurface
                }

                ListItem(
                    modifier = Modifier
                        .padding(start = if (cuenta.isHija) 16.dp else 0.dp)
                        .clickable(enabled = !cuenta.isAgrupadora) {
                            onCuentaSeleccionada(cuenta)
                        },
                    headlineContent = {
                        Text(
                            text = cuenta.nombre,
                            color = if (cuenta.agrupadora) colorTexto else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (cuenta.isAgrupadora) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                )

                //Si es la última cuenta o si la cuenta sieguiente es agrupadora o no es agrupadora pero es hija
                if (indice == cuentas.lastIndex || cuentas.getOrNull(indice + 1)?.let { it.agrupadora || it.padre == null } == true) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    })
}