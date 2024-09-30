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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planificadorapp.utilerias.generarColoresDesdeColoresBase
import java.text.NumberFormat
import java.util.Locale

/**
 * Composable que dibuja una gráfica de barras
 * V4
 */
@Composable
fun GraficaBarrasCanvas(
    modifier: Modifier = Modifier, datos: List<Pair<String, Double>>, titulo: String = ""
) {
    // Generar colores suaves utilizando el generador y la lista de colores base
    val colores =
        generarColoresDesdeColoresBase(datos.size, com.example.planificadorapp.ui.theme.baseColors)

    val valorMaximo =
        datos.maxOf { it.second } // Obtener el valor máximo para normalizar las barras
    val anchoMinimoBarra = 40.dp // Ancho mínimo de la barra
    val separacionEntreBarras = 40.dp // Separación entre barras
    val offsetInicial = 100.dp // Espacio de 100 dp antes de las barras y la línea del eje X

    // Calcular el ancho total del canvas en función del número de barras y la separación entre ellas
    val anchoTotalCanvas =
        (anchoMinimoBarra * datos.size) + (separacionEntreBarras * (datos.size - 1)) + offsetInicial + 20.dp //20 adicionales al final para que no se corten las etiquetas

    // Column para título y el contenido de la gráfica
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalAlignment = Alignment.Start
    ) {
        // Título de la gráfica
        if (titulo.isNotEmpty()) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Canvas dentro de un Box que permite scroll horizontal
        Box(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()) // Scroll horizontal
                .height(300.dp) // Altura de la gráfica
        ) {
            Canvas(
                modifier = Modifier
                    .width(anchoTotalCanvas) // El ancho del canvas se ajusta al número de barras
                    .height(300.dp) // Altura fija
            ) {
                val canvasHeight = size.height
                val barWidth = anchoMinimoBarra.toPx()  // Convertir el ancho mínimo a píxeles
                val spaceBetweenBars =
                    separacionEntreBarras.toPx() // Convertir la separación a píxeles
                val offsetInicialPx = offsetInicial.toPx()  // Convertir los 120 dp a píxeles

                // Número de líneas de referencia (6 líneas, con la quinta a la altura máxima)
                val numLineas = 6
                val distanciaEntreLineas = (canvasHeight / numLineas)

                // Ajustar los valores de las líneas de referencia para que estén en la misma proporción que los valores de las barras
                val valorIncremento =
                    valorMaximo / (numLineas - 1)  // Dividir el valor máximo en 5 partes iguales

                // Dibujar las líneas de referencia que atraviesan todo el canvas
                for (i in 0..numLineas) {
                    val yPos = canvasHeight - (i * distanciaEntreLineas)

                    // Dibujar línea de referencia a través de todo el canvas, incluyendo las barras
                    drawLine(
                        color = Color.Gray,
                        start = androidx.compose.ui.geometry.Offset(0f, yPos),
                        end = androidx.compose.ui.geometry.Offset(
                            size.width, yPos
                        ), // La línea se extiende hasta el final del canvas
                        strokeWidth = 2f
                    )

                    // Texto de la línea de referencia (a la izquierda)
                    drawContext.canvas.nativeCanvas.apply {
                        val valorReferencia = valorIncremento * i
                        val valorReferenciaFormatted =
                            NumberFormat.getCurrencyInstance(Locale("es", "MX"))
                                .format(valorReferencia)

                        drawText(valorReferenciaFormatted,
                            10f,
                            yPos - 10f, // Un poco por encima de la línea
                            android.graphics.Paint().apply {
                                textSize = 24f
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.LEFT
                            })
                    }
                }

                // Dibujar la línea del eje Y
                drawLine(
                    color = Color.Black, // Color más oscuro para diferenciar el Eje Y
                    start = androidx.compose.ui.geometry.Offset(
                        offsetInicialPx - 40, 0f
                    ), // Empieza desde la parte superior
                    end = androidx.compose.ui.geometry.Offset(
                        offsetInicialPx - 40, canvasHeight + 30
                    ), // Termina en la parte inferior
                    strokeWidth = 4f // Hacer la línea un poco más gruesa
                )

                // Dibujar la línea del eje X
                drawLine(
                    color = Color.Black, // Color más oscuro para diferenciar el Eje X
                    start = androidx.compose.ui.geometry.Offset(
                        0f, canvasHeight
                    ), // Comienza en el lado izquierdo
                    end = androidx.compose.ui.geometry.Offset(
                        size.width, canvasHeight
                    ), // Abarca todo el ancho del canvas
                    strokeWidth = 4f // Hacer la línea un poco más gruesa
                )

                datos.forEachIndexed { index, (mes, valor) ->
                    var color = colores[index % colores.size]
                    // Calcular la altura de la barra
                    val barHeight =
                        (valor / valorMaximo) * (canvasHeight - distanciaEntreLineas) // Ajustar para dejar espacio en la parte superior

                    // Calcular la posición X de la barra, añadiendo el offset inicial
                    val barX = offsetInicialPx + (index * (barWidth + spaceBetweenBars))

                    // Calcular la posición Y de la barra
                    val barY = canvasHeight - barHeight

                    // Dibujar la barra
                    if (valor > 0) {
                        drawRect(
                            color = color, // Color de la barra
                            topLeft = androidx.compose.ui.geometry.Offset(barX, barY.toFloat()),
                            size = androidx.compose.ui.geometry.Size(barWidth, barHeight.toFloat())
                        )
                    }

                    // Mostrar el valor encima de la barra
                    drawContext.canvas.nativeCanvas.apply {
                        val valorFormatted =
                            NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(valor)

                        drawText(valorFormatted,
                            barX + barWidth / 2,
                            (barY - 10f).toFloat(), // Un poco más arriba de la barra
                            android.graphics.Paint().apply {
                                textSize = 24f
                                color = Color.Black
                                textAlign = android.graphics.Paint.Align.CENTER
                            })

                        // Mostrar el mes debajo de la barra
                        drawText(mes.capitalize(),
                            barX + barWidth / 2,
                            canvasHeight + 30f, // Un poco más abajo del canvas para mostrar la leyenda
                            android.graphics.Paint().apply {
                                textSize = 24f
                                color = Color.Black
                                textAlign = android.graphics.Paint.Align.CENTER
                            })
                    }
                }
            }
        }
    }
}