package com.blackdeath.planificadorapp.modelos.composiciones

import com.blackdeath.planificadorapp.modelos.activos.ActivoModel
import com.blackdeath.planificadorapp.modelos.cuentas.CuentaModel

/**
 * Modelo de datos para guardar la representación de una composición de portafolio
 */
data class GuardarComposicionModel(
    var activo: ActivoModel,
    var porcentaje: Float = 0f,
    var cuentas: List<CuentaModel> = emptyList()
)