package com.example.planificadorapp.modelos

/**
 * Modelo de datos para guardar la representación de una composición de portafolio
 */
data class GuardarComposicionModel(
    var idActivo: Long = 0,
    var nombreActivo: String,
    var porcentaje: Float = 0f,
    var cuentas: List<Long> = emptyList()
)