package com.example.planificadorapp.pantallas.cuentas

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.composables.cuentas.CuentasListSimple
import com.example.planificadorapp.composables.fab.FloatingActionButtonActualizar
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.utilerias.FormatoFecha
import com.example.planificadorapp.utilerias.FormatoMonto

/**
 * Composable que representa la pantalla de detalle de una cuenta
 */
@Composable
fun DetalleCuentasScreen(
    modifier: Modifier = Modifier, navController: NavController, idCuenta: Long
) {
    val cuentaRepository = remember { CuentasRepository() }

    var cuenta by remember { mutableStateOf<CuentaModel?>(null) }
    var cuentasAgrupadas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var isCuentaAgrupadora by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val isFabVisible by remember { derivedStateOf { scrollState.value == 0 } }

    LaunchedEffect(idCuenta) {
        cuentaRepository.buscarCuentaPorId(idCuenta) { cuentaEncontrada ->
            if (cuentaEncontrada != null) {
                cuenta = cuentaEncontrada
                isCuentaAgrupadora = cuenta!!.agrupadora

                if (isCuentaAgrupadora) {
                    cuentaRepository.buscarSubcuentas(cuenta!!.id) { cuentasAgrupadasEncontradas ->
                        cuentasAgrupadas = cuentasAgrupadasEncontradas ?: emptyList()
                        Log.i(
                            "DetalleCuentasScreen",
                            "Cuentas agrupadas cargadas: ${cuentasAgrupadas.size}"
                        )
                    }
                }
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButtonActualizar(
            isVisible = isFabVisible,
            tooltip = "Actualizar la cuenta",
            onClick = {
                navController.navigate("cuentas/editar/${idCuenta}")
            })
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(bottom = 56.dp)
                .verticalScroll(scrollState)
        ) {
            cuenta?.let {
                val backgroundColor = if (isCuentaAgrupadora) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = backgroundColor,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = modifier.padding(16.dp)
                    ) {
                        if (isCuentaAgrupadora) {
                            AssistChip(
                                onClick = { },
                                label = { Text("Agrupadora") },
                                colors = AssistChipDefaults.assistChipColors(),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        if (it.padre != null) {
                            TextoConEtiqueta(
                                "Cuenta Agrupadora: ", it.padre.nombre, "large", "medium"
                            )
                        }
                        TextoConEtiqueta("Nombre: ", it.nombre, "large", "medium")
                        TextoConEtiqueta(
                            "Saldo: ", FormatoMonto.formato(it.saldo), "large", "medium"
                        )
                        TextoConEtiqueta("Descripción: ", it.descripcion ?: "", "large", "medium")
                        TextoConEtiqueta("Fecha de actualización: ",
                            it.fechaActualizacion?.let { FormatoFecha.formatoLargo(it) } ?: "",
                            "large",
                            "medium")
                    }
                }
            }

            if (isCuentaAgrupadora) {
                if (cuentasAgrupadas.isNotEmpty()) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = "Cuentas Agrupadas:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    CuentasListSimple(
                        modifier = Modifier.padding(horizontal = 16.dp), cuentas = cuentasAgrupadas
                    )
                } else {
                    Text(
                        modifier = modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        text = "Sin cuentas agrupadas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}