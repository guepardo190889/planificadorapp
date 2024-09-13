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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Composable que representa una gráfica de pastel en un canvas mejorada
 */
@Composable
fun GraficaPastelCanvas(
    modifier: Modifier = Modifier,
    titulo: String,
    datos: List<Pair<String, Float>>
) {
    val total = datos.sumOf { it.second.toDouble() }

    // Lista mejorada de colores suaves
    val colores = listOf(
        Color(0xFF1E88E5), Color(0xFF43A047), Color(0xFFFB8C00),
        Color(0xFFE53935), Color(0xFF8E24AA), Color(0xFF00ACC1)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
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

                // Dibujar sombra como efecto 3D
                drawArcWithShadow(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle.toFloat(),
                    size = size
                )

                // Dibujar etiquetas con porcentajes
                val porcentaje = (value / total) * 100
                val middleAngle = startAngle + sweepAngle / 2
                val labelX = center.x + (size.minDimension / 3) * cos(Math.toRadians(middleAngle.toDouble())).toFloat()
                val labelY = center.y + (size.minDimension / 3) * sin(Math.toRadians(middleAngle.toDouble())).toFloat()

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        String.format("%.1f%%", porcentaje),
                        labelX,
                        labelY,
                        android.graphics.Paint().apply {
                            textSize = 30f
                            color = Color.Black
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }

                // Actualizar el ángulo de inicio para la siguiente sección
                startAngle += sweepAngle.toFloat()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dibujar leyendas debajo de la gráfica
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            datos.forEachIndexed { index, (label, _) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    // Dibujar el rectángulo de color para la leyenda
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(colores[index % colores.size], shape = CircleShape)
                    )

                    // Mostrar el nombre de la leyenda
                    Text(text = label, style = TextStyle(fontSize = 14.sp))
                }
            }
        }
    }
}

/**
 * Función auxiliar para dibujar un arco con sombra en el Canvas
 */
fun DrawScope.drawArcWithShadow(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    size: Size
) {
    // Desplazar el arco un poco para simular sombra
    drawArc(
        color = color.copy(alpha = 0.5f), // Sombra
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        size = size,
        topLeft = Offset(5f, 5f) // Desplazar la sombra
    )

    // Dibujar el arco principal encima de la sombra
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        size = size
    )
}
