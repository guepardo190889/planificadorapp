package com.example.planificadorapp.screens.portafolios.guardado

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
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
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel

/**
 * Composable que representa la pantalla del tercer paso en el guardado de un portafolio.
 */
@Composable
fun GuardarPortafolioPasoTres(
    modifier: Modifier = Modifier,
    composiciones: List<GuardarComposicionModel>,
    cuentas: List<CuentaModel>,
    onAtrasClick: (List<GuardarComposicionModel>) -> Unit,
    onSiguienteClick: (List<GuardarComposicionModel>) -> Unit
) {
    var composicionesPasoTres by remember {
        mutableStateOf(composiciones)
    }

    var mostrarDialogoComposicionesSinCUentas by remember { mutableStateOf(false) }

    /**
     *Valida si al menos una composición no tiene cuentas asociadas
     */
    fun alMenosUnaComposicionNoTieneCuentasAsociadas(): Boolean {
        return composicionesPasoTres.any { it.cuentas.isEmpty() }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
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
                                onAtrasClick(
                                    composicionesPasoTres
                                )
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
                                if (alMenosUnaComposicionNoTieneCuentasAsociadas()) {
                                    mostrarDialogoComposicionesSinCUentas = true
                                } else {
                                    onSiguienteClick(
                                        composicionesPasoTres
                                    )
                                }
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = "Siguiente"
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Cuentas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(composicionesPasoTres) { composicion ->
                        ActivoCard(
                            composicion,
                            cuentas,
                            composicionesPasoTres,
                            onAgregarCuenta = { cuentaSeleccionada ->
                                composicionesPasoTres = composicionesPasoTres.map {
                                    if (it == composicion) {
                                        it.copy(cuentas = it.cuentas + cuentaSeleccionada)
                                    } else {
                                        it
                                    }
                                }
                            },
                            onEliminarCuenta = { cuentaSeleccionada ->
                                Log.i(
                                    "GuardarPortafolioPasoTres",
                                    "Cuenta seleccionada para eliminar: $cuentaSeleccionada"
                                )
                                composicionesPasoTres = composicionesPasoTres.map {
                                    if (it == composicion) {
                                        it.copy(cuentas = it.cuentas - cuentaSeleccionada)
                                    } else {
                                        it
                                    }
                                }
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            if (mostrarDialogoComposicionesSinCUentas) {
                ConfirmacionSimpleDialog(
                    texto = "Hay activos sin cuentas asociadas. ¿Deseas continuar?",
                    onDismissRequest = {
                        mostrarDialogoComposicionesSinCUentas = false
                    },
                    onConfirmar = {
                        onSiguienteClick(composicionesPasoTres)
                    })
            }
        }
    )
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
                ListItem(
                    headlineContent = {
                        Text(
                            text = cuenta.nombre,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = {
                            onEliminarCuenta(cuenta)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar Cuenta")
                        }
                    }
                )

            }

            Button(
                onClick = {
                    mostrarDialogoCuentas = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Agregar")
            }
        }
    }

    val cuentasDisponibles = cuentas.filterNot { cuenta ->
        composicionesPasoTres.any { it.cuentas.contains(cuenta) }
    }

    if (mostrarDialogoCuentas) {
        SeleccionarCuentaDialogo(
            cuentasDisponibles,
            onCuentaSeleccionada = { cuentaSeleccionada ->
                onAgregarCuenta(cuentaSeleccionada)
                mostrarDialogoCuentas = false
            },
            onDismissRequest = {
                mostrarDialogoCuentas = false
            }
        )
    }
}