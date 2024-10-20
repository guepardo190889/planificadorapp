package com.blackdeath.planificadorapp.modelos.portafolios.busqueda

import java.math.BigDecimal

/**
 * Modelo de datos que representa una cuenta en la búsqueda de un portafolio
 */
data class PortafolioBuscarCuentaResponseModel(
    val id: Long = 0,
    val nombre: String = "",
    val saldo: BigDecimal = BigDecimal.ZERO
)