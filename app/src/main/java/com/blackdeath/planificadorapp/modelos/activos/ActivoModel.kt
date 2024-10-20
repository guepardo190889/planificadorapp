package com.blackdeath.planificadorapp.modelos.activos

/**
 * Modelo de datos para representar un activo
 */
data class ActivoModel(
    var id: Long = 0,
    val nombre: String,
    val descripcion: String? = "",
    val padre: com.blackdeath.planificadorapp.modelos.activos.ActivoPadreResponseModel? = null
) {
    val isHijo: Boolean
        get() = padre != null

    val isPadre: Boolean
        get() = padre == null
}