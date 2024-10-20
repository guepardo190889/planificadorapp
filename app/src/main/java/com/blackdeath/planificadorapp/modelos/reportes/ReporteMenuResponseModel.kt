package com.blackdeath.planificadorapp.modelos.reportes

import com.blackdeath.planificadorapp.utilerias.enumeradores.CategoriaReporte

/**
 * Modelo de datos para representar el menú de reportes
 */
data class ReporteMenuResponseModel(
    val categoria: CategoriaReporte,
    var reportes:List<com.blackdeath.planificadorapp.modelos.reportes.ReporteModel> = emptyList()
)