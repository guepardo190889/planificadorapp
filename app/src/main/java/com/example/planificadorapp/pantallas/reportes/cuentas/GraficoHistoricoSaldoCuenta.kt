package com.example.planificadorapp.pantallas.reportes.cuentas

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
import com.example.planificadorapp.composables.AniosDropDown
import com.example.planificadorapp.composables.DividerConSubtitulo
import com.example.planificadorapp.composables.cuentas.CuentasDropDown
import com.example.planificadorapp.composables.graficos.GraficaBarrasCanvas
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.reportes.GraficoPastelModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.ReportesRepository
import java.time.Year

/**
 * Composable que dibuja una gráfica de barras para mostrar los saldos mensuales por cuenta
 */
@Composable
fun GraficoHistoricoSaldoCuenta(modifier: Modifier) {
    val cuentasRepository = CuentasRepository()
    val reportesRepository = ReportesRepository()

    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var cuentaSeleccionada by remember { mutableStateOf<CuentaModel?>(null) }
    var graficoHistoricoSaldoCuenta by remember { mutableStateOf<GraficoPastelModel?>(null) }
    var anioSeleccionado by remember { mutableStateOf<Int?>(Year.now().value) }

    //Busca el reporte de historico de saldos de la cuenta
    fun buscarReporte() {
        if (cuentaSeleccionada != null && anioSeleccionado != null) {
            reportesRepository.buscarReporteHistoricoSaldosCuenta(
                cuentaSeleccionada!!.id, anioSeleccionado!!
            ) { graficoHistoricoSaldoCuentaEncontrado ->
                if (graficoHistoricoSaldoCuentaEncontrado != null) {
                    graficoHistoricoSaldoCuenta = graficoHistoricoSaldoCuentaEncontrado
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        cuentasRepository.buscarCuentas(
            excluirCuentasAsociadas = false, incluirSoloCuentasNoAgrupadorasSinAgrupar = false
        ) { cuentasEncontradas ->
            cuentas = cuentasEncontradas ?: emptyList()
            Log.i("GraficoHistoricoSaldoCuenta", "Cuentas encontradas: ${cuentas.size}")
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
            CuentasDropDown(modifier = Modifier.fillMaxWidth(),
                etiqueta = "Selecciona una Cuenta",
                isCuentaAgrupadoraSeleccionable = false,
                cuentas = cuentas,
                mensajeError = "La cuenta es requerida",
                cuentaSeleccionada = cuentaSeleccionada,
                onCuentaSeleccionada = {
                    cuentaSeleccionada = it

                    buscarReporte()
                })

            AniosDropDown(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
                onAnioSeleccionado = {
                anioSeleccionado = it

                buscarReporte()
            })

            graficoHistoricoSaldoCuenta?.let { grafico ->
                DividerConSubtitulo(
                    subtitulo = "Saldo histórico mensual de la cuenta"
                )

                GraficaBarrasCanvas(
                    modifier = Modifier.fillMaxWidth(),
                    datos = grafico.datos
                )
            }
        }
    })
}