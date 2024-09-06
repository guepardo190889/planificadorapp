package com.example.planificadorapp.composables.graficos

import android.R
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.charts.Pie
import com.anychart.enums.LegendLayout


@Composable
fun GraficaPastel(data: List<Pair<String, Float>>){
    // Usamos AndroidView para integrar la vista de AnyChart en Compose
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            // Creamos la gráfica de pastel
            val pie: Pie = AnyChart.pie()

            // Los datos que usaremos en la gráfica
            val data: MutableList<DataEntry> = mutableListOf()
            data.add(ValueDataEntry("Acciones", 40))
            data.add(ValueDataEntry("Bonos", 30))
            data.add(ValueDataEntry("Fondos", 20))
            data.add(ValueDataEntry("Efectivo", 10))

            pie.data(data)
            pie.title("Distribución del Portafolio")

            // Crear la vista de AnyChart
            val chartView = com.anychart.AnyChartView(context)
            chartView.setChart(pie)
            chartView
        }
    )
}