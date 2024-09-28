package com.example.planificadorapp.composables.graficos

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planificadorapp.utilerias.enumeradores.TipoDatoGraficaPastel
import com.example.planificadorapp.utilerias.generarColoresDesdeColoresBase
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

/**
 * Composable que representa una gráfica de pastel en un canvas mejorada
 */
@Composable
fun GraficaPastelCanvas(
    modifier: Modifier,
    datos: List<Pair<String, Double>>,
    tipoDatoGrafica: TipoDatoGraficaPastel,
    titulo: String = "",
    isMostrarTotalDatos:Boolean = false
) {
    val total = datos.sumOf { it.second }

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
            .padding(8.dp),
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
            Log.i("GraficaPastelCanvas", "Dibujando gráfica de pastel")

            var startAngle = 0f
            datos.forEachIndexed { index, (_, value) ->
                val sweepAngle = (value / total) * 360f
                var color = colores[index % colores.size]

                Log.i("GraficaPastelCanvas", "Color: $color")

                // Dibujar el arco
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle.toFloat(),
                    useCenter = true,
                    size = size
                )

                // Dibujar etiquetas con porcentajes
                val middleAngle = startAngle + sweepAngle / 2
                val labelX =
                    center.x + (size.minDimension / 3) * cos(Math.toRadians(middleAngle)).toFloat()
                val labelY =
                    center.y + (size.minDimension / 3) * sin(Math.toRadians(middleAngle)).toFloat()

                // Etiqueta personalizada según el tipo de dato
                val textoEtiqueta = when (tipoDatoGrafica) {
                    TipoDatoGraficaPastel.NUMERO -> String.format(Locale("es", "MX"), "%.0f", value)
                    TipoDatoGraficaPastel.PORCENTAJE -> String.format(Locale("es", "MX"), "%.2f%%", (value / total) * 100)
                    TipoDatoGraficaPastel.DINERO -> NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(value)
                }

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        textoEtiqueta,
                        labelX,
                        labelY,
                        android.graphics.Paint().apply {
                            textSize = 26f
                            color = Color.Black
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }

                // Actualizar el ángulo de inicio para la siguiente sección
                startAngle += sweepAngle.toFloat()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .padding(8.dp)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Log.i("GraficaPastelCanvas", "Dibujando etiquetas")

            datos.forEachIndexed { index, (label, value) ->
                // Calcular el porcentaje del valor
                val porcentaje = (value / total) * 100

                // Formatear el valor según el tipo de dato
                val textoValor = when (tipoDatoGrafica) {
                    TipoDatoGraficaPastel.NUMERO -> String.format(Locale("es", "MX"), "%.0f", value)
                    TipoDatoGraficaPastel.PORCENTAJE -> String.format(Locale("es", "MX"), "%.2f%%", porcentaje)
                    TipoDatoGraficaPastel.DINERO -> NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(value)
                }

                // Formatear el porcentaje si es tipo número o dinero
                val textoPorcentaje = if (tipoDatoGrafica == TipoDatoGraficaPastel.NUMERO || tipoDatoGrafica == TipoDatoGraficaPastel.DINERO) {
                    String.format(Locale("es", "MX"), "%.2f%%", porcentaje)
                } else {
                    ""
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(colores[index % colores.size], shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Columna para el nombre de la leyenda (ocupa 2/3 de la fila)
                    Column(
                        modifier = Modifier.weight(2f / 3f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = label,
                            maxLines = 3,
                            style = TextStyle(fontSize = 13.sp)
                        )
                    }

                    // Columna para los valores (ocupa 1/3 de la fila)
                    Column(
                        modifier = Modifier.weight(1f / 3f),
                        horizontalAlignment = Alignment.End, // Alineación a la derecha
                        verticalArrangement = Arrangement.Top
                    ) {
                        // Fila para el valor
                        Text(
                            text = textoValor,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            maxLines = 1
                        )
                        // Fila para el porcentaje (solo si es número o dinero)
                        if (textoPorcentaje.isNotEmpty()) {
                            Text(
                                text = textoPorcentaje,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        if(isMostrarTotalDatos) {
            // Formatear el total según el tipo de dato
            val totalTexto = when (tipoDatoGrafica) {
                TipoDatoGraficaPastel.NUMERO -> String.format(Locale("es", "MX"), "%.0f", total)
                TipoDatoGraficaPastel.PORCENTAJE -> String.format(Locale("es", "MX"), "%.2f%%", total)
                TipoDatoGraficaPastel.DINERO -> NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(total)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.End // Alinear el total a la derecha
            ) {
                Text(
                    text = "Total: $totalTexto",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp)
                )
            }
        }

    }
}