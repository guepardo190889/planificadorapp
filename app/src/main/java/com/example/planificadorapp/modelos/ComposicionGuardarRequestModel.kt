package com.example.planificadorapp.modelos

/**
 * Modelo de datos para guardar las composiciones de un portafolio
 */
data class ComposicionGuardarRequestModel(
    var idActivo: Long,
    var porcentaje: Int,
    var idCuentas: List<Long>
)