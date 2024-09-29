package com.example.planificadorapp.composables.graficos

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

/**
 * Composable que dibuja una gráfica de barras
 */
@Composable
fun GraficaBarrasBasica(
    modifier: Modifier = Modifier, datos: List<Pair<String, Double>>, titulo: String = ""
) {
    val valorMaximo =
        datos.maxOf { it.second } // Obtener el valor máximo para normalizar las barras

    val anchoMinimoBarra = 60.dp // Ancho mínimo de la barra
    val separacionEntreBarras = 20.dp // Separación entre barras
    val offsetInicial = 80.dp // Espacio de 120 dp antes de las barras y la línea del eje X

    // Calcular el ancho total del canvas en función del número de barras y la separación entre ellas
    val anchoTotalCanvas =
        (anchoMinimoBarra * datos.size) + (separacionEntreBarras * (datos.size - 1)) + offsetInicial

    // Column para título y el contenido de la gráfica
    Column(
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

        // Canvas dentro de un Box que permite scroll horizontal
        Box(
            modifier = Modifier.horizontalScroll(rememberScrollState()) // Scroll horizontal
                .height(300.dp) // Altura de la gráfica
        ) {
            Canvas(
                modifier = Modifier
                    .width(anchoTotalCanvas) // El ancho del canvas se ajusta al número de barras
                    .height(300.dp) // Altura fija
            ) {
                val canvasHeight = size.height
                val barWidth = anchoMinimoBarra.toPx() // Convertir el ancho mínimo a píxeles
                val spaceBetweenBars =
                    separacionEntreBarras.toPx() // Convertir la separación a píxeles
                val offsetInicialPx = offsetInicial.toPx() // Convertir los 120 dp a píxeles

                // Añadir la línea del eje X en la parte inferior del canvas
                val posicionYLíneaEjeX = canvasHeight - 20f // Colocar el eje X 20 píxeles antes del borde inferior
                drawLine(
                    color = Color.Black,
                    start = Offset(offsetInicialPx, posicionYLíneaEjeX),
                    end = Offset(size.width, posicionYLíneaEjeX),
                    strokeWidth = 6f // Eje X más grueso
                )

                datos.forEachIndexed { index, (mes, valor) ->
                    // Calcular la altura de la barra
                    val barHeight = (valor / valorMaximo) * (canvasHeight - 40f) // Ajustar la altura para que no sobrepase el canvas

                    // Calcular la posición X de la barra
                    val barX = offsetInicialPx + (index * (barWidth + spaceBetweenBars))

                    // Calcular la posición Y de la barra
                    val barY = canvasHeight - barHeight - 20f // Alinear las barras con la línea del eje X

                    // Dibujar la barra
                    drawRect(
                        color = Color.Blue, // Color de la barra
                        topLeft = Offset(barX, barY.toFloat()),
                        size = Size(barWidth, barHeight.toFloat())
                    )

                    // Mostrar el valor encima de la barra
                    drawContext.canvas.nativeCanvas.apply {
                        val valorFormatted =
                            NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(valor)

                        drawText(valorFormatted,
                            barX + barWidth / 2,
                            (barY - 10f).toFloat(), // Un poco más arriba de la barra
                            android.graphics.Paint().apply {
                                textSize = 20f
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.CENTER
                            })

                        // Mostrar el mes debajo de la barra
                        drawText(mes.capitalize(),
                            barX + barWidth / 2,
                            canvasHeight + 30f, // Un poco más abajo del canvas para mostrar la leyenda
                            android.graphics.Paint().apply {
                                textSize = 24f
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.CENTER
                            })
                    }
                }
            }
        }
    }
}