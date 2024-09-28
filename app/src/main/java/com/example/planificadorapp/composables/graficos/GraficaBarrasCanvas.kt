package com.example.planificadorapp.composables.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planificadorapp.utilerias.generarColoresDesdeColoresBase
import java.text.NumberFormat
import java.util.Locale

/**
 * Composable que dibuja una gráfica de barras para mostrar los saldos mensuales
 */
@Composable
fun GraficaBarrasCanvas(
    modifier: Modifier = Modifier,
    datos: List<Pair<String, Double>>,
    titulo: String = ""
) {
    // Obtener el total máximo para normalizar la altura de las barras
    val total = datos.maxOf { it.second }

    val colores = generarColoresDesdeColoresBase(datos.size, com.example.planificadorapp.ui.theme.baseColors)

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la gráfica
        Text(
            text = titulo,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Canvas para dibujar la gráfica de barras
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = canvasWidth / (datos.size * 2) // Ancho de las barras

            val ejeX = 40f // Margen para Eje Y
            val ejeYAltura = 20f // Altura adicional para los textos de los meses
            val espacioInferior = 40f // Espacio debajo de la gráfica para los textos de los meses

            // Dibujar el Eje Y (Línea vertical)
            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(ejeX, 0f),
                end = androidx.compose.ui.geometry.Offset(ejeX, canvasHeight - ejeYAltura),
                strokeWidth = 4f
            )

            // Dibujar el Eje X (Línea horizontal)
            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(ejeX, canvasHeight - ejeYAltura),
                end = androidx.compose.ui.geometry.Offset(canvasWidth, canvasHeight - ejeYAltura),
                strokeWidth = 4f
            )

            datos.forEachIndexed { index, (mes, saldo) ->
// Normalizar la altura de la barra en relación con el saldo máximo
                val barHeight = (saldo / total) * (canvasHeight - espacioInferior)

                // Calcular las posiciones de la barra
                val barX = (index * 2 + 1) * barWidth + ejeX
                val barY = canvasHeight - espacioInferior - barHeight

                var color = colores[index % colores.size]

                // Dibujar la barra
                drawRect(
                    color = color,
                    topLeft = androidx.compose.ui.geometry.Offset(barX, barY.toFloat()),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight.toFloat())
                )

                // Mostrar el mes y el saldo en la barra
                drawContext.canvas.nativeCanvas.apply {
                    val saldoFormatted = NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(saldo)

                    // Mostrar el mes debajo de la barra
                    drawText(
                        mes.capitalize(),
                        barX + barWidth / 2,
                        canvasHeight + 24f,  // Un poco más abajo de la gráfica
                        android.graphics.Paint().apply {
                            textSize = 30f  // Texto más grande
                            color = Color.Black
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )

                    // Mostrar el saldo encima de la barra
                    drawText(
                        saldoFormatted,
                        barX + barWidth / 2,
                        barY.toFloat() - 10f,  // Un poco más arriba de la barra
                        android.graphics.Paint().apply {
                            textSize = 24f
                            color = Color.Black
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
        }
    }
}