package com.blackdeath.planificadorapp.modelos.activos

/**
 * Modelo de datos para guardar o actualizar un activo
 */
data class TransaccionActivoRequestModel(
    var nombre: String,
    var descripcion: String,
    var idPadre: Long
)