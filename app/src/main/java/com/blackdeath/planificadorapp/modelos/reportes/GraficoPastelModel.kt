package com.blackdeath.planificadorapp.modelos.reportes

/**
 * Modelo de datos para representar un gráfico de pastel
 */
data class GraficoPastelModel(
    val nombre:String,
    val datos:List<Pair<String,Double>>
)