package com.example.planificadorapp.modelos.composiciones

import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel

/**
 * Modelo de datos para guardar la representación de una composición de portafolio
 */
data class GuardarComposicionModel(
    var activo: ActivoModel,
    var porcentaje: Float = 0f,
    var cuentas: List<CuentaModel> = emptyList()
)