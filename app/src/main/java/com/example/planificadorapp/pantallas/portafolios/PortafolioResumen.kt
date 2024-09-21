package com.example.planificadorapp.pantallas.portafolios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel

/**
 * Composable que representa la pantalla de resumen del guardado o actualizado de un portafolio
 */
@Composable
fun ResumenPortafolio(
    modifier: Modifier = Modifier,
    nombre: String,
    descripcion: String,
    composiciones: List<GuardarComposicionModel>,
    isTransaccionGuardar:Boolean? = true,
    onAtrasClick: () -> Unit,
    onTransaccopmClick: () -> Unit,
) {
    Scaffold(bottomBar = {
        BarraNavegacionInferior(
            isTransaccionGuardar = isTransaccionGuardar,
            onAtrasClick = onAtrasClick, onTransaccionClick = onTransaccopmClick
        )
    }, content = { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            EncabezadoPortafolio(
                titulo = "Resumen"
            )

            Text(
                text = "Generales",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            TextoConEtiqueta(etiqueta = "Nombre: ", texto = nombre, "large", "medium")
            TextoConEtiqueta(etiqueta = "Descripción: ", texto = descripcion, "large", "medium")
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outline
            )

            Text(
                text = "Distribución",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(composiciones) { composicion ->
                    ResumenComposicion(composicion)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    })
}

/**
 * Composable que representa una tarjeta de composición en el resumen del guardado de un portafolio
 */
@Composable
fun ResumenComposicion(composicion: GuardarComposicionModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = composicion.activo.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Left
            )
            Text(
                text = "${composicion.porcentaje.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text(
            text = "Cuentas:", style = MaterialTheme.typography.titleSmall
        )

        if (composicion.cuentas.isNotEmpty()) {
            composicion.cuentas.forEach { cuenta ->
                Text(
                    text = cuenta.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        } else {
            Text(
                text = "No hay cuentas asociadas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}