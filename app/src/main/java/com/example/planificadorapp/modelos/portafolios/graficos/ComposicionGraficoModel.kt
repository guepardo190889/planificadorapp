package com.example.planificadorapp.modelos.portafolios.graficos

import java.math.BigDecimal

/**
 * Modelo de datos para representar una composición de un portafolio en un gráfico
 */
data class ComposicionGraficoModel(
    val nombreActivo: String,
    val porcentaje: Int,
    val saldoTotalCuentas: BigDecimal
)