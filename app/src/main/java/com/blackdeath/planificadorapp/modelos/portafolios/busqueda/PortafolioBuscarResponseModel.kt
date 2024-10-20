package com.blackdeath.planificadorapp.modelos.portafolios.busqueda

import java.math.BigDecimal

/**
 * Modelo de datos para la búsqueda individual de un portafolio
 */
data class PortafolioBuscarResponseModel(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val saldo: BigDecimal,
    val composiciones: List<com.blackdeath.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarComposicionResponseModel> = emptyList()
)