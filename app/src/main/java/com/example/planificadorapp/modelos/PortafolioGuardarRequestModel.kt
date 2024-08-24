package com.example.planificadorapp.modelos

/**
 * Modelo de datos para guardar un portafolio
 */
data class PortafolioGuardarRequestModel(
    val nombre: String,
    val descripcion: String,
    val composiciones: List<ComposicionGuardarRequestModel>
)