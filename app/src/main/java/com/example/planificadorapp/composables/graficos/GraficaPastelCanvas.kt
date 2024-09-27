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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planificadorapp.utilerias.generarColoresDesdeColoresBase
import java.util.Locale
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
    val colores = generarColoresDesdeColoresBase(datos.size, baseColors)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp), // Reducimos el padding para compactar la gráfica
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titulo, style = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .size(220.dp)
                .padding(8.dp)
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
                            textSize = 26f // Reducí el tamaño de texto de los porcentajes
                            color = Color.Black
                            textAlign = android.graphics.Paint.Align.CENTER
                        })
                }

                // Actualizar el ángulo de inicio para la siguiente sección
                startAngle += sweepAngle.toFloat()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .padding(8.dp)
                .padding(horizontal = 8.dp)
                .heightIn(max = 150.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            datos.forEachIndexed { index, dato ->
                val (label, value) = datos[index]
                val porcentaje = (value / total) * 100

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(colores[index % colores.size], shape = CircleShape)
                    )

                    // Textos del nombre del activo y el porcentaje/monto
                    Spacer(modifier = Modifier.width(8.dp))

                    // Textos ajustables dentro de dos terceras partes del ancho
                    Column(
                        modifier = Modifier.weight(2f / 3f)
                    ) {
                        Text(
                            text = label,
                            maxLines = 2,  // Permitimos que el nombre ocupe hasta 2 líneas si es necesario
                            style = TextStyle(fontSize = 13.sp)
                        )
                    }

                    // Texto del porcentaje y el monto, alineado a la derecha
                    Row(
                        modifier = Modifier.weight(1f / 3f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format(Locale("es", "MX"), "%.1f%%", porcentaje),
                            style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}