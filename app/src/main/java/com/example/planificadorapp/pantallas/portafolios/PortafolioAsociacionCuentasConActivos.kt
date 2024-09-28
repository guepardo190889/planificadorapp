package com.example.planificadorapp.pantallas.portafolios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.ConfirmacionSimpleDialog
import com.example.planificadorapp.composables.EncabezadoScreen
import com.example.planificadorapp.composables.cuentas.SeleccionarCuentaDialogo
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel

/**
 * Composable que representa la pantalla de asignación de cuentas a activos en un portafolio
 */
@Composable
fun PortafolioAsociacionCuentasConActivos(
    modifier: Modifier = Modifier,
    composiciones: List<GuardarComposicionModel>,
    cuentas: List<CuentaModel>,
    onAsociarCuenta: (GuardarComposicionModel, CuentaModel) -> Unit,
    onDesasociarCuenta: (GuardarComposicionModel, CuentaModel) -> Unit,
    onAtrasClick: () -> Unit,
    onSiguienteClick: () -> Unit
) {
    var mostrarDialogoComposicionesSinCuentas by remember { mutableStateOf(false) }

    Scaffold(bottomBar = {
        BarraNavegacionInferior(onAtrasClick = onAtrasClick, onSiguienteClick = {
            if (composiciones.any { it.cuentas.isEmpty() }) {
                mostrarDialogoComposicionesSinCuentas = true
            } else {
                onSiguienteClick()
            }
        })
    }, content = { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            EncabezadoScreen(
                titulo = "Asociación de Cuentas",
                descripcion = "Cuentas asociadas a los activos"
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(composiciones) { composicion ->
                    ComposicionItem(composicion = composicion,
                        cuentas = cuentas,
                        onAgregarCuenta = { cuentaSeleccionada ->
                            onAsociarCuenta(composicion, cuentaSeleccionada)
                        },
                        onEliminarCuenta = { cuentaSeleccionada ->
                            onDesasociarCuenta(composicion, cuentaSeleccionada)
                        })
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            if (mostrarDialogoComposicionesSinCuentas) {
                ConfirmacionSimpleDialog(texto = "Hay activos sin cuentas asociadas. ¿Deseas continuar?",
                    onDismissRequest = { mostrarDialogoComposicionesSinCuentas = false },
                    onConfirmar = { onSiguienteClick() })
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposicionItem(
    composicion: GuardarComposicionModel,
    cuentas: List<CuentaModel>,
    onAgregarCuenta: (CuentaModel) -> Unit,
    onEliminarCuenta: (CuentaModel) -> Unit
) {
    var isMostrarDialogoCuentas by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = composicion.activo.nombre,
                maxLines = 2,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )

            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text("Asociar cuenta") } },
                state = rememberTooltipState()
            ) {
                IconButton(onClick = { isMostrarDialogoCuentas = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Asociar Cuenta")
                }
            }
        }

        Text(
            text = if (composicion.cuentas.isEmpty()) "Sin cuentas asociadas" else "Cuentas:",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 2.dp)
        )

        composicion.cuentas.forEach { cuenta ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cuenta.nombre,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onEliminarCuenta(cuenta) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Cuenta")
                }
            }
        }

        if (isMostrarDialogoCuentas) {
            SeleccionarCuentaDialogo(cuentas, onCuentaSeleccionada = { cuentaSeleccionada ->
                onAgregarCuenta(cuentaSeleccionada)
                isMostrarDialogoCuentas = false
            }, onDismissRequest = { isMostrarDialogoCuentas = false })
        }
    }
}