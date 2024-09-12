package com.example.planificadorapp.modelos.cuentas

import java.math.BigDecimal

/**
 * Modelo de datos para guardar o actualizar una cuenta
 */
data class ActualizarCuentaRequestModel(
    var nombre: String,
    var descripcion: String,
    var saldo: BigDecimal = BigDecimal.ZERO,
    var idsCuentasAgrupadas: List<Long> = emptyList()
)