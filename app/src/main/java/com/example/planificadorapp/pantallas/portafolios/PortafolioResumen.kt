package com.example.planificadorapp.pantallas.portafolios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.TextoEtiquetado
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
    isTransaccionGuardar: Boolean? = true,
    onAtrasClick: () -> Unit,
    onTransaccionClick: () -> Unit,
) {
    Scaffold(bottomBar = {
        BarraNavegacionInferior(
            isTransaccionGuardar = isTransaccionGuardar,
            onAtrasClick = onAtrasClick,
            onTransaccionClick = onTransaccionClick
        )
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            EncabezadoPortafolio(
                titulo = "Resumen",
                descripcion = "Resumen de la información del portafolio"
            )

            // Sección de información general
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Generales",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextoEtiquetado(
                etiqueta = "Nombre: ",
                texto = nombre,
                estiloEtiqueta = MaterialTheme.typography.bodyMedium,
                estiloTexto = MaterialTheme.typography.bodyLarge
            )
            TextoEtiquetado(
                etiqueta = "Descripción: ",
                texto = descripcion,
                estiloEtiqueta = MaterialTheme.typography.bodyMedium,
                estiloTexto = MaterialTheme.typography.bodyLarge
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.outline
            )

            // Sección de distribuciones
            Text(
                text = "Distribución",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                composiciones.forEach { composicion ->
                    ResumenComposicion(composicion)
                }
            }
        }
    }
}

/**
 * Composable que representa una tarjeta de composición en el resumen del guardado de un portafolio
 */
@Composable
fun ResumenComposicion(composicion: GuardarComposicionModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = composicion.activo.nombre,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${composicion.porcentaje.toInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "Cuentas:",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (composicion.cuentas.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                composicion.cuentas.forEach { cuenta ->
                    Text(
                        text = cuenta.nombre,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        } else {
            Text(
                text = "No hay cuentas asociadas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}