package com.example.planificadorapp.composables.graficos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie

/**
 * Composable que representa una gráfica de pastel
 */
@Composable
fun GraficaPastel(modifier: Modifier, titulo: String, datos: List<Pair<String, Float>>) {
    AndroidView(modifier = modifier.fillMaxSize(), factory = { context ->
        // Crear la vista de AnyChart
        val chartView = com.anychart.AnyChartView(context)

        // Inicializar el gráfico de pastel
        val pie: Pie = AnyChart.pie()

        // Convertir los datos para el gráfico
        val chartData: MutableList<DataEntry> = mutableListOf()
        datos.forEach { (label, value) ->
            chartData.add(ValueDataEntry(label, value))
        }

        pie.data(chartData)
        pie.title(titulo)

        // Configurar el gráfico en la vista
        chartView.setChart(pie)

        chartView // Devolver la vista creada
    })
}