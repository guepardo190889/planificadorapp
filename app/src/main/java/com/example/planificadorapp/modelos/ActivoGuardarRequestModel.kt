package com.example.planificadorapp.modelos

/**
 * Modelo de datos para guardar un activo
 */
data class ActivoGuardarRequestModel(
    var nombre: String,
    var descripcion: String,
    var idPadre: Long
)