package com.example.planificadorapp.pantallas.portafolios

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import com.example.planificadorapp.composables.ConfirmacionSimpleDialog
import com.example.planificadorapp.composables.cuentas.SeleccionarCuentaDialogo
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel

/**
 * Composable que representa la pantalla de asignación de cuentas a activos en un portafolio
 */
@Composable
fun PortafolioAsignacionCuentasConActivos(
    modifier: Modifier = Modifier,
    composiciones: List<GuardarComposicionModel>,
    cuentas: List<CuentaModel>,
    onAsignarCuenta: (GuardarComposicionModel, CuentaModel) -> Unit,
    onDesasignarCuenta: (GuardarComposicionModel, CuentaModel) -> Unit,
    onAtrasClick: () -> Unit,
    onSiguienteClick: () -> Unit
) {
    var mostrarDialogoComposicionesSinCuentas by remember { mutableStateOf(false) }

    /**
     *Valida si al menos una composición no tiene cuentas asociadas
     */
    fun alMenosUnaComposicionNoTieneCuentasAsociadas(): Boolean {
        return composiciones.any { it.cuentas.isEmpty() }
    }

    Scaffold(bottomBar = {
        BarraNavegacionInferior(onAtrasClick = onAtrasClick, onSiguienteClick = {
            if (alMenosUnaComposicionNoTieneCuentasAsociadas()) {
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
        ) {
            EncabezadoPortafolio(titulo = "Asignación de Cuentas")

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(composiciones) { composicion ->
                    ActivoCard(composicion = composicion,
                        cuentas = cuentas,
                        composiciones,
                        onAgregarCuenta = { cuentaSeleccionada ->
                            onAsignarCuenta(composicion, cuentaSeleccionada)
                        },
                        onEliminarCuenta = { cuentaSeleccionada ->
                            onDesasignarCuenta(composicion, cuentaSeleccionada)
                        })
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }

        if (mostrarDialogoComposicionesSinCuentas) {
            ConfirmacionSimpleDialog(texto = "Hay activos sin cuentas asociadas. ¿Deseas continuar?",
                onDismissRequest = {
                    mostrarDialogoComposicionesSinCuentas = false
                },
                onConfirmar = {
                    onSiguienteClick()
                })
        }
    })
}

@Composable
fun ActivoCard(
    composicion: GuardarComposicionModel,
    cuentas: List<CuentaModel>,
    composicionesPasoTres: List<GuardarComposicionModel>,
    onAgregarCuenta: (CuentaModel) -> Unit,
    onEliminarCuenta: (CuentaModel) -> Unit
) {
    var mostrarDialogoCuentas by remember { mutableStateOf(false) }
    val cuentasAsociadas = cuentas.filter { composicion.cuentas.contains(it) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = composicion.activo.nombre,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            cuentasAsociadas.forEach { cuenta ->
                ListItem(headlineContent = {
                    Text(
                        text = cuenta.nombre,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }, trailingContent = {
                    IconButton(onClick = {
                        onEliminarCuenta(cuenta)
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Cuenta")
                    }
                })

            }

            Button(
                onClick = {
                    mostrarDialogoCuentas = true
                }, modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Agregar")
            }
        }
    }

    val cuentasDisponibles = cuentas.filterNot { cuenta ->
        composicionesPasoTres.any { it.cuentas.contains(cuenta) }
    }

    if (mostrarDialogoCuentas) {
        SeleccionarCuentaDialogo(cuentasDisponibles, onCuentaSeleccionada = { cuentaSeleccionada ->
            onAgregarCuenta(cuentaSeleccionada)
            mostrarDialogoCuentas = false
        }, onDismissRequest = {
            mostrarDialogoCuentas = false
        })
    }
}