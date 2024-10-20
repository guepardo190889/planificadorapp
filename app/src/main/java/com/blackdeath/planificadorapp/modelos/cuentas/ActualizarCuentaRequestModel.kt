package com.blackdeath.planificadorapp.modelos.cuentas

import java.math.BigDecimal

/**
 * Modelo de datos para guardar o actualizar una cuenta
 */
data class ActualizarCuentaRequestModel(
    var nombre: String,
    var descripcion: String,
    var saldo: BigDecimal? = null,
    var idsCuentasAgrupadas: List<Long> = emptyList()
)