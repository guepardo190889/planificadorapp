package com.blackdeath.planificadorapp.modelos.portafolios.busqueda

/**
 * Modelo de datos que representa una composición en la búsqueda de un portafolio
 */
data class PortafolioBuscarComposicionResponseModel(
    val idComposicion: Long,
    val idActivo: Long,
    val nombreActivo: String,
    val porcentaje: Int,
    val cuentas: List<com.blackdeath.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarCuentaResponseModel> = emptyList()
)