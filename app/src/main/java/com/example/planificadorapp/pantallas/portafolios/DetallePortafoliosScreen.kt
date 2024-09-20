package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.composables.graficos.GraficaPastelCanvas
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarComposicionResponseModel
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarCuentaResponseModel
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarResponseModel
import com.example.planificadorapp.modelos.portafolios.graficos.DistribucionPortafolioGraficoModel
import com.example.planificadorapp.repositorios.PortafoliosRepository
import com.example.planificadorapp.utilerias.FormatoMonto

/**
 * Composable que representa la pantalla de detalles de un portafolio
 */
@Composable
fun DetallePortafoliosScreen(
    modifier: Modifier = Modifier, navController: NavController, idPortafolio: Long
) {
    val portafoliosRepository = PortafoliosRepository()
    var portafolio by remember { mutableStateOf<PortafolioBuscarResponseModel?>(null) }
    var datosGrafico by remember { mutableStateOf<DistribucionPortafolioGraficoModel?>(null) }

    val scrollState = rememberLazyListState()
    val isFabVisible by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }

    LaunchedEffect(idPortafolio) {
        portafoliosRepository.buscarPortafolioPorId(idPortafolio) { portafolioEncontrado ->
            portafolio = portafolioEncontrado
            portafoliosRepository.buscarDatosGraficoDistribucion(idPortafolio) {
                datosGrafico = it
            }
        }
    }

    Scaffold(floatingActionButton = {
        AnimatedVisibility(
            visible = isFabVisible,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("portafolios/editar/${idPortafolio}") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Actualizar Portafolio")
            }
        }
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp), state = scrollState
        ) {
            item {
                portafolio?.let { portafolio ->
                    // Card for general info
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            TextoConEtiqueta("Nombre: ", portafolio.nombre ?: "", "large", "medium")
                            TextoConEtiqueta(
                                "Saldo: ", FormatoMonto.formato(portafolio.saldo), "large", "medium"
                            )
                            TextoConEtiqueta(
                                "Descripción: ", portafolio.descripcion ?: "", "large", "medium"
                            )
                        }
                    }
                }
            }

            item {
                portafolio?.let { portafolio ->
                    ComposicionesList(composiciones = portafolio.composiciones)
                }
            }

            item {
                datosGrafico?.let { grafico ->
                    if (grafico.composicionesPortafolio.isNotEmpty()) {
                        val datos = grafico.composicionesPortafolio.map {
                            it.nombreActivo to it.saldoTotalCuentas.toFloat()
                        }

                        GraficaPastelCanvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp), // Compactando el espacio entre los componentes
                            titulo = "Distribución", datos = datos
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ComposicionesList(composiciones: List<PortafolioBuscarComposicionResponseModel>) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        composiciones.forEach { composicion ->
            var mostrarCuentas by remember { mutableStateOf(false) }
            val saldoTotalActivo = composicion.cuentas.sumOf { it.saldo }

            Column(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = composicion.nombreActivo ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = FormatoMonto.formato(saldoTotalActivo),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Slider(
                        value = composicion.porcentaje / 100f,
                        onValueChange = {},
                        valueRange = 0f..1f,
                        enabled = false,
                        modifier = Modifier
                            .weight(1f)
                            .height(16.dp)
                    )
                    Text(
                        text = "${composicion.porcentaje} %",
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clickable { mostrarCuentas = !mostrarCuentas },
                    text = if (mostrarCuentas) "Ocultar cuentas" else "Ver cuentas",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Blue, textDecoration = TextDecoration.Underline
                    )
                )
            }

            if (mostrarCuentas) {
                Log.i("cuentas", composicion.cuentas.toString())
                if (composicion.cuentas.isNotEmpty()) {
                    Log.i("Cuentas not empty", "not empty")
                    CuentasDetalleList(cuentas = composicion.cuentas)
                } else {
                    Log.i("Cuentas empty", "empty")
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Sin cuentas",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Divider entre composiciones
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
fun CuentasDetalleList(cuentas: List<PortafolioBuscarCuentaResponseModel>) {
    Column(modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)) {
        cuentas.forEach { cuenta ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cuenta.nombre ?: "Sin nombre",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = FormatoMonto.formato(cuenta.saldo),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}