package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.composables.graficos.GraficaPastel
import com.example.planificadorapp.composables.graficos.GraficaPastelCanvas
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
    val portafoliosRepository = remember { PortafoliosRepository() }
    var datosGrafico by remember { mutableStateOf<DistribucionPortafolioGraficoModel?>(null) }

    LaunchedEffect(idPortafolio) {
        portafoliosRepository.buscarDatosGraficoDistribucion(idPortafolio) {
            datosGrafico = it
            Log.i("DetallePortafoliosScreen", "Datos del portafolio: $it")
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
    }, content = {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            if (datosGrafico != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        TextoConEtiqueta(
                            "Nombre: ", datosGrafico!!.nombrePortafolio, "large", "large"
                        )

                        TextoConEtiqueta(
                            "Saldo: ",
                            FormatoMonto.formato(BigDecimal("1000000.00")),
                            "large",
                            "large"
                        )

                        TextoConEtiqueta(
                            "Descripción: ", "Descripción de mi portafolio", "large", "large"
                        )
                    }
                }

                val datos = mutableListOf<Pair<String, Float>>()
                for (composicion in datosGrafico!!.composicionesPortafolio) {
                    datos.add(
                        Pair(
                            composicion.nombreActivo, composicion.saldoTotalCuentas.toFloat()
                        )
                    )
                }

                Log.i("DetallePortafoliosScreen", "Datos del portafolio: $datos")

                GraficaPastel(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally) // Centrando la gráfica
                        .padding(16.dp) // Añadir un margen alrededor de la gráfica
                        .widthIn(min = 200.dp, max = 400.dp) // Restricción de ancho
                        .heightIn(min = 200.dp, max = 300.dp), // Restricción de altura
                    titulo = "Distribución", datos
                )

                GraficaPastelCanvas(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally) // Centrando la gráfica
                        .padding(16.dp), // Añadir un margen alrededor de la gráfica
                        //.widthIn(min = 200.dp, max = 400.dp) // Restricción de ancho
                        //.heightIn(min = 200.dp, max = 300.dp), // Restricción de altura
                    titulo = "Distribución", datos
                )
            }
        }
    })
}