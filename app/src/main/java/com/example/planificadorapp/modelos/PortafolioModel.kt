package com.example.planificadorapp.modelos

/**
 * Modelo de datos para representar un portafolio
 */
data class PortafolioModel(
    var id: Long? = null,
    val nombre: String,
    val descripcion: String,
)