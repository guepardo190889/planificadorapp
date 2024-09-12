package com.example.planificadorapp.modelos.cuentas

import java.math.BigDecimal

/**
 * Modelo de datos para guardar una cuenta
 */
data class GuardarCuentaRequestModel(
    var nombre: String,
    var descripcion: String,
    var saldo: BigDecimal = BigDecimal.ZERO,
    var agrupadora: Boolean = false,
    var idsCuentasAgrupadas: List<Long> = emptyList()
)
