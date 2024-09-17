package com.example.planificadorapp.pantallas.portafolios.actualizado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel

@Composable
fun ActualizarPortafolioPasoTres(
    modifier: Modifier = Modifier,
    composiciones: List<GuardarComposicionModel>,
    cuentas: List<CuentaModel>,
    onAtrasClick: (List<GuardarComposicionModel>) -> Unit,
    onSiguienteClick: (List<GuardarComposicionModel>) -> Unit
) {
    var composicionesPasoTres by remember { mutableStateOf(composiciones) }

    var mostrarDialogoComposicionesSinCuentas by remember { mutableStateOf(false) }

    fun validarCuentas(): Boolean {
        return composicionesPasoTres.all { it.cuentas.isNotEmpty() }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar (
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                content = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                onAtrasClick(composicionesPasoTres)
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
                                if (!validarCuentas()) {
                                    mostrarDialogoComposicionesSinCuentas = true
                                } else {
                                    onSiguienteClick(composicionesPasoTres)
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
            Column (
                modifier = modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Cuentas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                /*ComposicionesListWithCuentas(
                    composiciones = composicionesPasoTres,
                    cuentasDisponibles = cuentas,
                    onAgregarCuenta = { composicion, cuenta ->
                        composicionesPasoTres = composicionesPasoTres.map {
                            if (it == composicion) {
                                it.copy(cuentas = it.cuentas + cuenta)
                            } else {
                                it
                            }
                        }
                    },
                    onEliminarCuenta = { composicion, cuenta ->
                        composicionesPasoTres = composicionesPasoTres.map {
                            if (it == composicion) {
                                it.copy(cuentas = it.cuentas - cuenta)
                            } else {
                                it
                            }
                        }
                    }
                )*/
            }
        }
    )
}