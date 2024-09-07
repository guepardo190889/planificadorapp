package com.example.planificadorapp.screens.portafolios.guardado

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.activos.ActivoModel

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
                    itemsIndexed(activos) { indice, activo ->
                        ListItem(
                            headlineContent = {
                                if (activo.padre == null) {
                                    // Activo padre
                                    Text(
                                        text = activo.nombre,
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                                    )
                                } else {
                                    // Activo hijo
                                    Row {
                                        Spacer(modifier = Modifier.width(16.dp)) // Indentación para los activos hijos
                                        Text(text = activo.nombre)
                                    }
                                }
                            },
                            modifier = Modifier.clickable {
                                onActivoSeleccionado(activo)
                            }
                        )

                        // Añadir separador entre grupos de activos padre e hijos
                        val isUltimoElemento = indice == activos.size - 1
                        val isProximoPadre = !isUltimoElemento && activos[indice + 1].padre == null

                        if (isUltimoElemento || isProximoPadre) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    )
}