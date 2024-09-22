package com.example.planificadorapp.modelos.activos

/**
 * Modelo de datos para representar un activo
 */
data class ActivoModel(
    var id: Long = 0,
    val nombre: String,
    val descripcion: String? = "",
    val padre: ActivoPadreResponseModel? = null
)