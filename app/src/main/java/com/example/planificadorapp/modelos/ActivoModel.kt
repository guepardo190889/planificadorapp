package com.example.planificadorapp.modelos

/**
 * Modelo de datos para representar un activo
 */
data class ActivoModel(
    var id: Long? = null,
    val nombre: String,
    val descripcion: String,
    var porcentaje: Float = 0f
)