package com.example.planificadorapp.screens.portafolios.guardado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.modelos.GuardarComposicionModel

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

    var idCuentasSeleccionadas: List<Long> by remember { mutableStateOf(emptyList()) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
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
                            onClick = {
                                onSiguienteClick(
                                    composicionesPasoTres
                                )
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "Cuentas", style = MaterialTheme.typography.headlineMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            composicionesPasoTres.forEach { composicion ->
                ActivoCard(
                    composicion,
                    cuentas,
                    composicionesPasoTres,
                    onAgregarCuenta = { cuentaSeleccionada ->
                        composicionesPasoTres = composicionesPasoTres.map {
                            if (it.idActivo == composicion.idActivo) {
                                it.copy(cuentas = it.cuentas + cuentaSeleccionada.id)
                            } else {
                                it
                            }
                        }
                    })
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun ActivoCard(
    composicion: GuardarComposicionModel,
    cuentas: List<CuentaModel>,
    composicionesPasoTres: List<GuardarComposicionModel>,
    onAgregarCuenta: (CuentaModel) -> Unit
) {
    var mostrarDialogoCuentas by remember { mutableStateOf(false) }
    val cuentasAsociadas = cuentas.filter { composicion.cuentas.contains(it.id) }

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
                text = composicion.nombreActivo,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            cuentasAsociadas.forEach { cuenta ->
                Text(
                    text = cuenta.nombre,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
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
        composicionesPasoTres.any { it.cuentas.contains(cuenta.id) }
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