package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.planificadorapp.composables.fab.FloatingActionButtonActualizar
import com.example.planificadorapp.composables.graficos.GraficaPastelCanvas
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarComposicionResponseModel
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarCuentaResponseModel
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarResponseModel
import com.example.planificadorapp.modelos.portafolios.graficos.DistribucionPortafolioGraficoModel
import com.example.planificadorapp.repositorios.PortafoliosRepository
import com.example.planificadorapp.utilerias.FormatoMonto
import com.example.planificadorapp.utilerias.enumeradores.TipoDatoGraficaPastel

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

    val scrollState = rememberScrollState()
    val isFabVisible by remember { derivedStateOf { scrollState.value == 0 } }

    LaunchedEffect(idPortafolio) {
        portafoliosRepository.buscarPortafolioPorId(idPortafolio) { portafolioEncontrado ->
            portafolio = portafolioEncontrado
            portafoliosRepository.buscarDatosGraficoDistribucion(idPortafolio) {
                datosGrafico = it
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxWidth(), floatingActionButton = {
        FloatingActionButtonActualizar(
            isVisible = isFabVisible,
            tooltip = "Actualizar el portafolio",
            onClick = {
                navController.navigate("portafolios/editar/${idPortafolio}")
            })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(bottom = 56.dp)
                .verticalScroll(scrollState)
        ) {
            portafolio?.let { portafolio ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        TextoConEtiqueta("Nombre: ", portafolio.nombre ?: "", "large", "medium")
                        TextoConEtiqueta(
                            "Saldo: ", FormatoMonto.formato(portafolio.saldo), "large", "medium"
                        )
                        TextoConEtiqueta(
                            "Descripción: ", portafolio.descripcion ?: "", "large", "medium"
                        )
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        text = "Distribución",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline
                    )

                    ComposicionesList(composiciones = portafolio.composiciones)

                    datosGrafico?.let { grafico ->
                        if (grafico.composicionesPortafolio.isNotEmpty()) {
                            val datos = grafico.composicionesPortafolio.map {
                                it.nombreActivo to it.saldoTotalCuentas.toDouble()
                            }

                            GraficaPastelCanvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp), // Compactando el espacio entre los componentes
                                titulo = "Distribución de activos",
                                datos = datos,
                                tipoDatoGrafica = TipoDatoGraficaPastel.PORCENTAJE
                            )
                        }
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
                    text = cuenta.nombre ?: "",
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