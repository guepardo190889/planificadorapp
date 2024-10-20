package com.blackdeath.planificadorapp.pantallas.reportes.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blackdeath.planificadorapp.utilerias.enumeradores.TipoDatoGraficaPastel
import com.blackdeath.planificadorapp.composables.DividerConSubtitulo
import com.blackdeath.planificadorapp.composables.graficos.GraficaPastelCanvas
import com.blackdeath.planificadorapp.composables.portafolios.PortafoliosDropDown
import com.blackdeath.planificadorapp.modelos.portafolios.PortafolioModel
import com.blackdeath.planificadorapp.modelos.reportes.GraficoPastelModel
import com.blackdeath.planificadorapp.repositorios.PortafoliosRepository
import com.blackdeath.planificadorapp.repositorios.ReportesRepository

/**
 * Composable que representa el gráfico de distribución de saldos de un portafolio
 */
@Composable
fun GraficoDistribucionSaldo(modifier: Modifier){
    val portafoliosRepository = PortafoliosRepository()
    val reportesRepository = ReportesRepository()

    var portafolios by remember { mutableStateOf<List<PortafolioModel>>(emptyList()) }
    var portafolioSeleccionado by remember { mutableStateOf<PortafolioModel?>(null) }
    var graficoDistribucionSaldo by remember { mutableStateOf<GraficoPastelModel?>(null) }

    LaunchedEffect(Unit) {
        portafoliosRepository.buscarPortafolios { portafoliosEncontrados ->
            portafolios = portafoliosEncontrados ?: emptyList()
            Log.i("GraficoDistribucionSaldo", "Portafolios encontrados: ${portafolios.size}")
        }
    }

    Scaffold(modifier = modifier.fillMaxWidth(), content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
                .padding(bottom = 56.dp)
                .verticalScroll(rememberScrollState())
        ) {
            PortafoliosDropDown(modifier = Modifier.fillMaxWidth(),
                etiqueta = "Selecciona un Portafolio",
                portafolios = portafolios,
                onPortafolioSeleccionado = {
                    portafolioSeleccionado = it

                    it.id?.let { id ->
                        reportesRepository.buscarReporteDistribucionSaldosPortafolio(id) { graficoDistribucionActivoEncontrado ->
                            if (graficoDistribucionActivoEncontrado != null) {
                                graficoDistribucionSaldo = graficoDistribucionActivoEncontrado
                            }
                        }
                    }
                })

            graficoDistribucionSaldo?.let { grafico ->
                DividerConSubtitulo(
                    subtitulo = "Distribución de saldos por activo en el portafolio"
                )

                GraficaPastelCanvas(
                    modifier = Modifier.fillMaxWidth(),
                    datos = grafico.datos,
                    tipoDatoGrafica = TipoDatoGraficaPastel.DINERO,
                    isMostrarTotalDatos = true
                )
            }
        }
    })
}