package com.blackdeath.planificadorapp.modelos.portafolios

import com.blackdeath.planificadorapp.modelos.composiciones.ComposicionGuardarRequestModel

/**
 * Modelo de datos para guardar un portafolio
 */
data class PortafolioGuardarRequestModel(
    val nombre: String,
    val descripcion: String,
    val composiciones: List<ComposicionGuardarRequestModel>
)