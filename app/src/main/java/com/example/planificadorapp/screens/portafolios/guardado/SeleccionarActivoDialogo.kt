package com.example.planificadorapp.screens.portafolios.guardado

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.ActivoModel

@Composable
fun SeleccionarActivoDialogo(
    activos: List<ActivoModel>,
    onActivoSeleccionado: (ActivoModel) -> Unit,
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
                Text(text = "Selecciona un Activo", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(activos) { activo ->
                        ListItem(
                            headlineContent = { Text(text = activo.nombre) },
                            modifier = Modifier.clickable {
                                onActivoSeleccionado(activo)
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    )
}