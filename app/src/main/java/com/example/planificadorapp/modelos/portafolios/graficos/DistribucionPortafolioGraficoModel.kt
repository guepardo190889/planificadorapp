package com.example.planificadorapp.modelos.portafolios.graficos

/**
 * Modelo de datos para representar una distribución de un portafolio en un gráfico
 */
data class DistribucionPortafolioGraficoModel(
    val nombrePortafolio: String,
    val composicionesPortafolio: List<ComposicionGraficoModel>
)