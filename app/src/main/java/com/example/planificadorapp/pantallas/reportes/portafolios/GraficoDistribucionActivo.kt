package com.example.planificadorapp.pantallas.reportes.portafolios

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
import com.example.planificadorapp.composables.DividerConSubtitulo
import com.example.planificadorapp.composables.graficos.GraficaPastelCanvas
import com.example.planificadorapp.composables.portafolios.PortafoliosDropDown
import com.example.planificadorapp.modelos.portafolios.PortafolioModel
import com.example.planificadorapp.modelos.reportes.GraficoPastelModel
import com.example.planificadorapp.repositorios.PortafoliosRepository
import com.example.planificadorapp.repositorios.ReportesRepository

/**
 * Composable que representa el gráfico de distribución de activos de un portafolio
 */
@Composable
fun GraficoDistribucionActivo(modifier: Modifier) {
    val portafoliosRepository = PortafoliosRepository()
    val reportesRepository = ReportesRepository()

    var portafolios by remember { mutableStateOf<List<PortafolioModel>>(emptyList()) }
    var portafolioSeleccionado by remember { mutableStateOf<PortafolioModel?>(null) }
    var graficoDistribucionActivo by remember { mutableStateOf<GraficoPastelModel?>(null) }

    LaunchedEffect(Unit) {
        portafoliosRepository.buscarPortafolios { portafoliosEncontrados ->
            portafolios = portafoliosEncontrados ?: emptyList()
            Log.i("GraficoDistribucionActivo", "Portafolios encontrados: ${portafolios.size}")
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
                        reportesRepository.generarReporteDistribucionActivos(id) { graficoDistribucionActivoEncontrado ->
                            if (graficoDistribucionActivoEncontrado != null) {
                                graficoDistribucionActivo = graficoDistribucionActivoEncontrado
                            }
                        }
                    }
                })

            graficoDistribucionActivo?.let { grafico ->
                DividerConSubtitulo(
                    subtitulo = "Distribución porcentual de los activos en el portafolio"
                )

                GraficaPastelCanvas(
                    modifier = Modifier.fillMaxWidth(), titulo = "", datos = grafico.datos
                )
            }
        }
    })
}