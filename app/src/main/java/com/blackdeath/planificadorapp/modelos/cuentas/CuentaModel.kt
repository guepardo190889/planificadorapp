package com.blackdeath.planificadorapp.modelos.cuentas

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Modelo de datos para representar una cuenta
 */
data class CuentaModel(
    var id: Long = 0,
    val nombre: String,
    val descripcion: String? = "",
    val saldo: BigDecimal,
    var fechaActualizacion: LocalDateTime? = null,
    val padre: com.blackdeath.planificadorapp.modelos.cuentas.CuentaPadreResponseModel? = null,
    val agrupadora: Boolean = false,
    var seleccionada: Boolean = false
) {
    val isHija: Boolean
        get() = padre != null

    val isAgrupadora: Boolean
        get() = agrupadora
}