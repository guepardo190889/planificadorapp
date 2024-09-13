package com.example.planificadorapp.utilerias

import androidx.compose.ui.graphics.Color

/**
 * Generador de colores suaves para gráficos, basado en la paleta actual del tema.
 */
fun GeneradorColor(n: Int, baseColors: List<Color>): List<Color> {
    val coloresGenerados = mutableListOf<Color>()

    // Si el número de colores necesarios es menor o igual a los colores base, usamos solo esos.
    if (n <= baseColors.size) {
        return baseColors.take(n)
    }

    // De lo contrario, generamos colores interpolados entre los colores base.
    val step = baseColors.size.toFloat() / n
    for (i in 0 until n) {
        val index = (i * step).toInt()
        val nextIndex = (index + 1) % baseColors.size
        val t = (i * step) - index

        // Interpolamos entre el color actual y el siguiente.
        val colorInterpolado = interpolarColores(baseColors[index], baseColors[nextIndex], t)
        coloresGenerados.add(colorInterpolado)
    }

    return coloresGenerados
}

/**
 * Función auxiliar que interpola dos colores.
 */
private fun interpolarColores(color1: Color, color2: Color, factor: Float): Color {
    val r = (color1.red + factor * (color2.red - color1.red)).coerceIn(0f, 1f)
    val g = (color1.green + factor * (color2.green - color1.green)).coerceIn(0f, 1f)
    val b = (color1.blue + factor * (color2.blue - color1.blue)).coerceIn(0f, 1f)
    val a = (color1.alpha + factor * (color2.alpha - color1.alpha)).coerceIn(0f, 1f)
    return Color(r, g, b, a)
}