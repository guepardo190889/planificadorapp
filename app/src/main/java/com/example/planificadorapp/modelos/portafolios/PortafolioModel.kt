package com.example.planificadorapp.modelos.portafolios

/**
 * Modelo de datos para representar un portafolio
 */
data class PortafolioModel(
    var id: Long? = null,
    val nombre: String,
    val descripcion: String,
)