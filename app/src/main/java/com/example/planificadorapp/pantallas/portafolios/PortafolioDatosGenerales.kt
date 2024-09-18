package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.OutlinedTextFieldBase
import com.example.planificadorapp.utilerias.validadores.PortafolioValidador

/**
 * Composable que contiene los campos generales (nombre y descripción) de un portafolio
 */
@Composable
fun PortafolioDatosGenerales(
    modifier: Modifier = Modifier,
    nombre: String,
    descripcion: String,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onSiguienteClick: () -> Unit
) {
    var isNombreValido by remember { mutableStateOf(true) }

    Scaffold(bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingActionButton(
                        modifier = Modifier.padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = {
                            Log.i("PortafolioDatosGenerales", "isNombreValido: $isNombreValido")
                            isNombreValido = PortafolioValidador.validarNombre(nombre)

                            if (isNombreValido) {
                                onSiguienteClick()
                            }
                        },
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = "Siguiente"
                        )
                    }
                }
            })
    }, content = { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
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

            OutlinedTextFieldBase(
                value = nombre,
                label = "Nombre",
                isError = !isNombreValido,
                errorMessage = "El nombre es requerido",
                supportingText = null,
                maxLength = 20,
                onValueChange = { nombreActualizado ->
                    isNombreValido = PortafolioValidador.validarNombre(nombreActualizado)
                    onNombreChange(nombreActualizado)
                },
            )

            OutlinedTextFieldBase(
                value = descripcion,
                label = "Descripción",
                maxLength = 256,
                onValueChange = onDescripcionChange
            )
        }
    })
}