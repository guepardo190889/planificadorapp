package com.example.planificadorapp.modelos

/**
 * Modelo de datos para guardar o actualizar un activo
 */
data class TransaccionActivoRequestModel(
    var nombre: String,
    var descripcion: String,
    var idPadre: Long
)