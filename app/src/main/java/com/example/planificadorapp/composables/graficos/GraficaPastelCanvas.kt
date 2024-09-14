package com.example.planificadorapp.composables.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planificadorapp.utilerias.FormatoMonto
import com.example.planificadorapp.utilerias.GeneradorColor
import kotlin.math.cos
import kotlin.math.sin

/**
 * Composable que representa una gráfica de pastel en un canvas mejorada
 */
@Composable
fun GraficaPastelCanvas(
    modifier: Modifier, titulo: String, datos: List<Pair<String, Float>>
) {
    val total = datos.sumOf { it.second.toDouble() }

    // Lista de colores base obtenidos de tu clase `Color.kt`
    val baseColors = listOf(
        Color(0xFF406836),  // primaryLight
        Color(0xFF54634D),  // secondaryLight
        Color(0xFF386568),  // tertiaryLight
        Color(0xFFBA1A1A),  // errorLight
        Color(0xFFC0EFAF),  // primaryContainerLight
        Color(0xFFD7E8CD)   // secondaryContainerLight
    )

    // Generar colores suaves utilizando el generador y la lista de colores base
    val colores = GeneradorColor(datos.size, baseColors)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar el título de la gráfica
        Text(text = titulo, style = TextStyle(fontSize = 20.sp))

        Spacer(modifier = Modifier.height(16.dp))

        // Dibujar la gráfica de pastel
        Canvas(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp)
        ) {
            var startAngle = 0f
            datos.forEachIndexed { index, (_, value) ->
                val sweepAngle = (value / total) * 360f
                var color = colores[index % colores.size]

                // Dibujar el arco
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle.toFloat(),
                    useCenter = true,
                    size = size
                )

                // Dibujar etiquetas con porcentajes
                val porcentaje = (value / total) * 100
                val middleAngle = startAngle + sweepAngle / 2
                val labelX =
                    center.x + (size.minDimension / 3) * cos(Math.toRadians(middleAngle)).toFloat()
                val labelY =
                    center.y + (size.minDimension / 3) * sin(Math.toRadians(middleAngle)).toFloat()

                drawContext.canvas.nativeCanvas.apply {
                    drawText(String.format("%.1f%%", porcentaje),
                        labelX,
                        labelY,
                        android.graphics.Paint().apply {
                            textSize = 30f
                            color = Color.Black
                            textAlign = android.graphics.Paint.Align.CENTER
                        })
                }

                // Actualizar el ángulo de inicio para la siguiente sección
                startAngle += sweepAngle.toFloat()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dibujar leyendas debajo de la gráfica
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .heightIn(max = 200.dp)
        ) {
            items(datos.size) { index ->
                val (label, value) = datos[index]
                val porcentaje = (value / total) * 100

                ListItem(headlineContent = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Dibujar el círculo de color para la leyenda
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(colores[index % colores.size], shape = CircleShape)
                        )

                        // Mostrar el nombre de la leyenda
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = label,
                            maxLines = 2,
                            style = TextStyle(fontSize = 14.sp)
                        )

                        // Mostrar el valor
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text =  FormatoMonto.formato(value.toDouble()),
                            style = TextStyle(fontSize = 14.sp)
                        )

                        // Mostrar el porcentaje
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = String.format("%.1f%%", porcentaje),
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                })
            }
        }
    }
}
