package com.example.planificadorapp.composables.graficos

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planificadorapp.utilerias.generarColoresDesdeColoresBase
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Composable que dibuja una gráfica de barras para mostrar los saldos mensuales
 */
@Composable
fun GraficaBarrasCanvas(
    modifier: Modifier = Modifier,
    datos: List<Pair<String, Double>>,
    titulo: String = ""
) {
    val anchoBarra = 30.dp
    val espacioMinimoEntreBarras = 20.dp
    val colores = generarColoresDesdeColoresBase(datos.size, com.example.planificadorapp.ui.theme.baseColors)

    val valorMaximo = datos.maxOf { it.second }
    val valorMinimo = datos.minOf { it.second }
    val cantidadLineasReferencia = 5
    val intervalo = (valorMaximo / 4).roundToInt()

    val alturaLeyendas = 120.dp
    val alturaTotalCanvas = 300.dp + alturaLeyendas

    val anchoLeyendasReferencia = 120.dp
    val anchoTotalCanvas = anchoLeyendasReferencia + (anchoBarra * datos.size) + ((espacioMinimoEntreBarras * (datos.size - 1)))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TituloGraficaBarraCanvas(titulo)

        Box(modifier = Modifier
            .border(1.dp, Color.Blue)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())) {

            Canvas(
                modifier = Modifier
                    .border(3.dp, Color.Red)
                    .fillMaxWidth()
                    .height(alturaTotalCanvas)
                    .width(anchoTotalCanvas)
            ) {
                // Ajustar la altura para las barras, considerando el espacio para las leyendas
                val posicionYMaximaInferiorLineasReferencia = size.height - alturaLeyendas.toPx() - 10 // -10 para margen inferior con leyendas
                var posicionYMaximaSuperiorLineasReferencia = 0f
                Log.i("GraficaBarrasCanvas", "posicionMinimaInferiorLineasReferencia: $posicionYMaximaInferiorLineasReferencia")

                // Dibujar las líneas de referencia
                for (i in 0..cantidadLineasReferencia) {
                    val posicionXLeyendaReferencia = 0f
                    val posicionYLeyendaReferencia = (posicionYMaximaInferiorLineasReferencia - (i * intervalo)) - 10f //-10 para estar sobre la línea
                    val leyendaReferencia = NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(i * intervalo)

                    Log.i("GraficaBarrasCanvas", "posicionY: $posicionYLeyendaReferencia - posicionX: $posicionXLeyendaReferencia - leyenda: $leyendaReferencia")

                    // Dibujar leyendas de referencias
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(leyendaReferencia,
                            posicionXLeyendaReferencia, posicionYLeyendaReferencia,
                            android.graphics.Paint().apply {
                                textSize = 24f
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.LEFT
                            }
                        )
                    }

                    val posicionXInicioLineaReferencia = 0f
                    val posicionXFinLineaReferencia = 120f
                    val posicionYLineaReferencia = posicionYMaximaInferiorLineasReferencia - (i * intervalo)

                    //Dibujar líneas de referencia
                    drawLine(
                        color = Color.Gray,
                        start = androidx.compose.ui.geometry.Offset(posicionXInicioLineaReferencia, posicionYLineaReferencia),
                        end = androidx.compose.ui.geometry.Offset(posicionXFinLineaReferencia, posicionYLineaReferencia),
                        strokeWidth = 2f
                    )

                    if(i == 5) {
                        posicionYMaximaSuperiorLineasReferencia = posicionYLineaReferencia
                    }
                }

                // Añadir la línea del eje X en la parte inferior
                val posicionYLineaEjeX = size.height - alturaLeyendas.toPx() - 10 // -10 para margen inferior con leyendas
                val posicionXinicialLineaEjeX = 0f
                val opsicionXFinalLineaEjeX = size.width // Abarca todo el ancho del canvas

                drawLine(
                    color = Color.Black, // Color más oscuro para diferenciar el Eje X
                    start = androidx.compose.ui.geometry.Offset(posicionXinicialLineaEjeX, posicionYLineaEjeX),
                    end = androidx.compose.ui.geometry.Offset(opsicionXFinalLineaEjeX, posicionYLineaEjeX),
                    strokeWidth = 3f
                )

                // Dibujar las barras
                val valorPixel = valorMaximo / (posicionYMaximaInferiorLineasReferencia - (posicionYMaximaSuperiorLineasReferencia-intervalo))

                datos.forEachIndexed { index, (mes, saldo) ->
                    val posicionXBarra = anchoLeyendasReferencia.toPx() + (index * (anchoBarra.toPx() + espacioMinimoEntreBarras.toPx()))

                    if (saldo >= 0) {
                        // Solo graficar barras con saldo positivo o cero
                        val alturaBarra = saldo / valorPixel
                        val posicionYBarra = posicionYLineaEjeX - alturaBarra

                        var color = colores[index % colores.size]

                        // Dibujar la barra
                        drawRect(
                            color = color,
                            topLeft = androidx.compose.ui.geometry.Offset(posicionXBarra, posicionYBarra.toFloat()),
                            size = androidx.compose.ui.geometry.Size(anchoBarra.toPx(), alturaBarra.toFloat())
                        )

                        // Mostrar el saldo encima de la barra
                        /*drawContext.canvas.nativeCanvas.apply {
                            val saldoFormatted = NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(saldo)

                            // Mostrar el saldo encima de la barra
                            drawText(
                                saldoFormatted,
                                posicionXBarra + anchoBarra.toPx() / 2,
                                barY.toFloat() - 10f,  // Un poco más arriba de la barra
                                android.graphics.Paint().apply {
                                    textSize = 24f
                                    color = Color.Black
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                            )
                        }*/
                    }

                    // Mostrar el mes debajo de la barra, sin importar si el saldo es negativo o positivo
                    /*drawContext.canvas.nativeCanvas.apply {
                        save()
                        rotate(270f, barX + anchoBarra.toPx() / 2, alturaLineasReferencia + 40f)
                        drawText(
                            mes.capitalize(),
                            barX + anchoBarra.toPx() / 2,
                            alturaLineasReferencia + 70f,  // Un poco más abajo de la gráfica
                            android.graphics.Paint().apply {
                                textSize = 28f  // Texto más grande para los meses
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                        restore()

                        // Mostrar el saldo negativo (solo el texto, sin la barra)
                        if (saldo < 0) {
                            val saldoFormatted = NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(saldo)

                            // Mostrar el saldo negativo donde correspondería la barra
                            drawText(
                                saldoFormatted,
                                barX + anchoBarra.toPx() / 2,
                                alturaLineasReferencia - 10f,  // Justo encima de la línea del eje X
                                android.graphics.Paint().apply {
                                    textSize = 24f
                                    color = android.graphics.Color.RED // Color rojo para resaltar saldo negativo
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                            )
                        }
                    }*/
                }
            }
        }
    }
}

@Composable
fun TituloGraficaBarraCanvas(titulo:String){
    Text(
        text = titulo,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}