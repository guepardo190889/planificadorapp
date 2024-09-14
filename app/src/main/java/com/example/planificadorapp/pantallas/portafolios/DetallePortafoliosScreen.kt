package com.example.planificadorapp.pantallas.portafolios

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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

    LaunchedEffect(idPortafolio) {
        portafoliosRepository.buscarPortafolioPorId(idPortafolio) { portafolioEncontrado ->
            portafolio = portafolioEncontrado
            portafoliosRepository.buscarDatosGraficoDistribucion(idPortafolio) {
                datosGrafico = it
            }
        }
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = { /* Edit action */ },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Actualizar Portafolio")
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            portafolio?.let { portafolio ->
                // Card for general info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextoConEtiqueta("Nombre", portafolio.nombre ?: "", "large", "medium")
                        TextoConEtiqueta(
                            "Saldo", FormatoMonto.formato(portafolio.saldo), "large", "medium"
                        )
                        TextoConEtiqueta("Descripción", portafolio.descripcion, "large", "medium")
                    }
                }

                // Activos y composiciones
                ComposicionesList(composiciones = portafolio.composiciones)
            }

            // Gráfico de pastel
            datosGrafico?.let { grafico ->
                if (grafico.composicionesPortafolio.isNotEmpty()) {
                    val datos = grafico.composicionesPortafolio.map {
                        it.nombreActivo to it.saldoTotalCuentas.toFloat()
                    }

                    GraficaPastelCanvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        titulo = "Distribución", datos = datos
                    )
                }
            }
        }
    }
}

@Composable
fun ComposicionesList(composiciones: List<PortafolioBuscarComposicionResponseModel>) {
    Column(modifier = Modifier.padding(16.dp)) {
        composiciones.forEach { composicion ->
            ListItem(
                headlineContent = { Text(text = composicion.nombreActivo ?: "") },
                trailingContent = { Text(text = "${composicion.porcentaje} %") }
            )
            CuentasList(cuentas = composicion.cuentas)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun CuentasList(cuentas: List<PortafolioBuscarCuentaResponseModel>) {
    if (cuentas.isNotEmpty()) {
        cuentas.forEach { cuenta ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = cuenta.nombre ?: "", modifier = Modifier.weight(1f))
                Text(
                    text = FormatoMonto.formato(cuenta.saldo),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }
    } else {
        Text(text = "No hay cuentas asociadas", style = MaterialTheme.typography.bodySmall)
    }
}