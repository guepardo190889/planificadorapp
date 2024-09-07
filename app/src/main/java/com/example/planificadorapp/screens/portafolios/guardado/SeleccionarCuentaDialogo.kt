package com.example.planificadorapp.screens.portafolios.guardado

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.cuentas.CuentaModel

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
                Text("Cerrar")
            }
        },
        text = {
            Column {
                Text(text = "Selecciona una Cuenta", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                cuentas.forEach { cuenta ->
                    ListItem(
                        headlineContent = { Text(text = cuenta.nombre) },
                        modifier = Modifier.clickable {
                            onCuentaSeleccionada(cuenta)
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    )
}