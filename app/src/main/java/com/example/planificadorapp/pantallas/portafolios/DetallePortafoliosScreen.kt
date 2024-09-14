package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.composables.graficos.GraficaPastelCanvas
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarResponseModel
import com.example.planificadorapp.modelos.portafolios.graficos.DistribucionPortafolioGraficoModel
import com.example.planificadorapp.repositorios.PortafoliosRepository
import com.example.planificadorapp.utilerias.FormatoMonto
import java.math.BigDecimal

/**
 * Composable que representa la pantalla de detalle de un portafolio
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
            if (portafolioEncontrado != null) {
                portafolio = portafolioEncontrado
                Log.i("DetallePortafoliosScreen", "Portafolio encontrado: $portafolio")

                portafoliosRepository.buscarDatosGraficoDistribucion(idPortafolio) {
                    datosGrafico = it
                    Log.i("DetallePortafoliosScreen", "Datos del portafolio: $it")
                }
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxWidth(), floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            onClick = {
                //navController.navigate("portafolios/editar/${idPortafolio}")
            },
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Actualizar Portafolio")
        }
    }, content = { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .border(2.dp, MaterialTheme.colorScheme.primary),
        ) {
            portafolio?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        portafolio?.let {
                            TextoConEtiqueta(
                                etiqueta = "Nombre: ",
                                texto = it.nombre ?: "",
                                styleLabel = "large",
                                styleBody = "medium"
                            )
                            TextoConEtiqueta(
                                etiqueta = "Saldo: ",
                                texto = FormatoMonto.formato(it.saldo),
                                styleLabel = "large",
                                styleBody = "medium"
                            )
                            TextoConEtiqueta(
                                etiqueta = "Descripción: ",
                                texto = it.descripcion,
                                styleLabel = "large",
                                styleBody = "medium"
                            )
                        }
                    }
                }

                Column(modifier = modifier.border(2.dp, Color.Yellow)) {
                    Text(
                        modifier = modifier.padding(16.dp).border(2.dp, Color.Black), text = "Activos: "
                    )
                    //Composiciones
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        items(it.composiciones) { composicion ->
                            ListItem(modifier = modifier,
                                //.border(2.dp, MaterialTheme.colorScheme.error),
                                headlineContent = {
                                    Text(
                                        text = composicion.nombreActivo ?: "",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }, trailingContent = {
                                    Text(
                                        text = composicion.porcentaje.toString() + " %",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                })

                            Column(
                                modifier = modifier.padding(16.dp)
                            ) {
                                if (composicion.cuentas.isNotEmpty()) {
                                    Text(
                                        modifier = modifier,//.border(2.dp, Color.Green),
                                        text = "Cuentas asociadas: ",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                    composicion.cuentas.forEach { cuenta ->
                                        Row(modifier = modifier.fillMaxWidth()) {
                                            Text(
                                                modifier = modifier.weight(1f),
                                                //.border(2.dp, Color.Blue),
                                                text = cuenta.nombre ?: "",
                                                textAlign = TextAlign.Left,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface,
                                            )
                                            Text(
                                                modifier = modifier.weight(1f),
                                                //.border(2.dp, Color.Cyan),
                                                text = FormatoMonto.formato(cuenta.saldo),
                                                textAlign = TextAlign.End,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface,
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "No hay cuentas asociadas",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }

            }

            if (datosGrafico != null) {
                if (datosGrafico!!.composicionesPortafolio.isNotEmpty()) {
                    val datos = mutableListOf<Pair<String, Float>>()
                    for (composicion in datosGrafico!!.composicionesPortafolio) {
                        datos.add(
                            Pair(
                                composicion.nombreActivo, composicion.saldoTotalCuentas.toFloat()
                            )
                        )
                    }

                    Log.i("DetallePortafoliosScreen", "Datos del portafolio: $datos")

                    GraficaPastelCanvas(
                        modifier = modifier, titulo = "Distribución", datos
                    )
                }
            }
        }
    })
}