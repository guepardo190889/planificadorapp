package com.example.planificadorapp.screens.portafolios.guardado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.modelos.composiciones.ComposicionGuardarRequestModel
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.modelos.portafolios.PortafolioGuardarRequestModel

/**
 * Composable que representa la pantalla de resumen del guardado de un portafolio
 */
@Composable
fun GuardarPortafolioResumen(
    modifier: Modifier,
    nombre: String,
    descripcion: String,
    composiciones: List<GuardarComposicionModel>,
    onAtrasClick: () -> Unit,
    onGuardarClick: (PortafolioGuardarRequestModel) -> Unit
) {
    /**
     * Función que crea un modelo de datos para guardar un portafolio
     */
    fun crearModeloGuardado(): PortafolioGuardarRequestModel {
        val composicionesPorGuardar = mutableListOf<ComposicionGuardarRequestModel>()

        for (composicion in composiciones) {
            val cuentasPorGuardar = mutableListOf<Long>()

            for (cuenta in composicion.cuentas) {
                cuentasPorGuardar.add(cuenta.id)
            }

            composicionesPorGuardar.add(
                ComposicionGuardarRequestModel(
                    composicion.activo.id,
                    composicion.porcentaje.toInt(),
                    cuentasPorGuardar
                )
            )
        }

        val portafolio = PortafolioGuardarRequestModel(nombre, descripcion, composicionesPorGuardar)

        return portafolio
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = modifier.fillMaxWidth(),
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
                                onAtrasClick()
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Atrás"
                            )
                        }

                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                val modeloGuardado = crearModeloGuardado()
                                onGuardarClick(modeloGuardado)
                            }
                        ) {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = "Guardar"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Resumen",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outline
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
                text = "Activos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(composiciones) { composicion ->
                    ResumenComposicionCard(composicion)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

/**
 * Composable que representa una tarjeta de composición en el resumen del guardado de un portafolio
 */
@Composable
fun ResumenComposicionCard(composicion: GuardarComposicionModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = composicion.activo.nombre,
                    style = MaterialTheme.typography.bodyMedium,
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
                text = "Cuentas:",
                style = MaterialTheme.typography.titleSmall
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
}