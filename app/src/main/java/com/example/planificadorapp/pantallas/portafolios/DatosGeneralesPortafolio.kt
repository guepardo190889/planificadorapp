package com.example.planificadorapp.pantallas.portafolios

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Composable que contiene los campos generales (nombre y descripción) de un portafolio
 */
@Composable
fun PortafolioDatosGenerales(
    modifier: Modifier = Modifier,
    nombre: MutableState<String>,
    descripcion: MutableState<String>,
    isNombreValido: MutableState<Boolean>,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit
) {
    Text(
        text = "Generales",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.outline
    )

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(value = nombre.value,
            onValueChange = {
                onNombreChange(it)
            },
            label = { Text("Nombre") },
            isError = !isNombreValido.value,
            singleLine = true,
            textStyle = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (!isNombreValido.value) {
                    Text(
                        text = "El nombre es requerido",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${nombre.value.length}/20",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End
                )
            })

        OutlinedTextField(value = descripcion.value,
            onValueChange = {
                onDescripcionChange(it)
            },
            label = { Text("Descripción") },
            textStyle = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${descripcion.value.length}/256",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End
                )
            })
    }
}