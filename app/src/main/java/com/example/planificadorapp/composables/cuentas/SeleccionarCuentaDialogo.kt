package com.example.planificadorapp.composables.cuentas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "Cerrar",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Selecciona una Cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                cuentas.forEachIndexed { indice, cuenta ->
                    val colorTexto = if (cuenta.isAgrupadora) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = if (cuenta.isHija) 16.dp else 0.dp)
                            .clickable(enabled = !cuenta.isAgrupadora) {
                                onCuentaSeleccionada(cuenta)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = cuenta.nombre,
                            color = colorTexto,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (cuenta.isAgrupadora) FontWeight.Bold else FontWeight.Normal
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (indice == cuentas.lastIndex || cuentas.getOrNull(indice + 1)
                            ?.let { it.agrupadora || it.padre == null } == true
                    ) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            }
        }
    )
}