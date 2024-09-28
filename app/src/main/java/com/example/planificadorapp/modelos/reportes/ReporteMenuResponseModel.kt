package com.example.planificadorapp.modelos.reportes

import com.example.planificadorapp.utilerias.enumeradores.CategoriaReporte

/**
 * Modelo de datos para representar el menú de reportes
 */
data class ReporteMenuResponseModel(
    val categoria: CategoriaReporte,
    var reportes:List<ReporteModel> = emptyList()
)