package com.blackdeath.planificadorapp.composables.graficos

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blackdeath.planificadorapp.ui.theme.coloresBaseGraficas
import com.blackdeath.planificadorapp.utilerias.generarColoresDesdeColoresBase
import java.text.NumberFormat
import java.util.Locale

/**
 * Composable que representa una gráfica de barras en un Canvas
 * V9
 */
@Composable
fun GraficaBarrasCanvas(
    modifier: Modifier = Modifier, datos: List<Pair<String, Double>>, titulo: String = ""
) {
    // Generar colores suaves utilizando el generador
    val colores = generarColoresDesdeColoresBase(datos.size, coloresBaseGraficas)

    // Valores de la gráfica
    val valorMaximo = datos.maxOf { it.second }
    val anchoMinimoBarra = 40.dp
    val separacionEntreBarras = 40.dp
    val offsetInicial = 100.dp
    val anchoTotalCanvas =
        (anchoMinimoBarra * datos.size) + (separacionEntreBarras * (datos.size - 1)) + offsetInicial + 20.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalAlignment = Alignment.Start
    ) {
        if (titulo.isNotEmpty()) {
            Text(text = titulo, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .height(300.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .width(anchoTotalCanvas)
                    .height(300.dp)
            ) {
                val canvasHeight = size.height
                val barWidth = anchoMinimoBarra.toPx()
                val spaceBetweenBars = separacionEntreBarras.toPx()
                val offsetInicialPx = offsetInicial.toPx()
                val numLineas = 6
                val distanciaEntreLineas = (canvasHeight / numLineas)
                val valorIncremento = valorMaximo / (numLineas - 1)
                val paint = android.graphics.Paint().apply {
                    textSize = 24f
                    color = android.graphics.Color.BLACK
                    textAlign = Align.LEFT
                }

                // Dibujar las líneas de referencia
                for (i in 0..numLineas) {
                    val yPos = canvasHeight - (i * distanciaEntreLineas)
                    val valorReferencia = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
                        .format(valorIncremento * i)

                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, yPos),
                        end = Offset(size.width, yPos),
                        strokeWidth = 2f
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        valorReferencia, 10f, yPos - 10f, paint
                    )
                }

                // Dibujar ejes
                drawLine(
                    color = Color.Black, start = Offset(offsetInicialPx - 40, 0f), end = Offset(
                        offsetInicialPx - 40, canvasHeight + 30
                    ), strokeWidth = 4f
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, canvasHeight),
                    end = Offset(size.width, canvasHeight),
                    strokeWidth = 4f
                )

                // Dibujar las barras
                datos.forEachIndexed { index, (mes, valor) ->
                    val barX = offsetInicialPx + (index * (barWidth + spaceBetweenBars))
                    val barHeight =
                        ((valor / valorMaximo) * (canvasHeight - distanciaEntreLineas)).toFloat()
                    val barY = (canvasHeight - barHeight)
                    val valorFormatted =
                        NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(valor)
                    if (valor > 0) {
                        drawRect(
                            color = colores[index % colores.size],
                            topLeft = Offset(barX, barY),
                            size = Size(barWidth, barHeight)
                        )
                    }

                    // Texto de valor sobre la barra
                    drawContext.canvas.nativeCanvas.drawText(valorFormatted,
                        barX + barWidth / 2,
                        barY - 10f,
                        paint.apply { textAlign = Align.CENTER })

                    Log.i(
                        "GraficaBarrasCanvas",
                        "Valor formateado: $valorFormatted , x: ${barX + barWidth / 2}, y: ${barY - 10f}"
                    )

                    // Texto del mes debajo de la barra
                    drawContext.canvas.nativeCanvas.drawText(
                        mes, barX + barWidth / 2, canvasHeight + 30f, paint
                    )
                    Log.i(
                        "GraficaBarrasCanvas",
                        "mes: $mes ,x: ${barX + barWidth / 2}, y: ${canvasHeight + 30f}"
                    )

                }
            }
        }
    }
}