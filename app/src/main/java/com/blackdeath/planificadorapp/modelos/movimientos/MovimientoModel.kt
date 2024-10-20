package com.blackdeath.planificadorapp.modelos.movimientos

import com.blackdeath.planificadorapp.utilerias.enumeradores.TipoMovimiento
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Modelo de datos para representar un movimiento
 */
data class MovimientoModel(
    var id: Long = 0,
    val concepto: String,
    val descripcion: String,
    val monto: BigDecimal,
    val fecha: LocalDate,
    val idCuenta: Long = 0,
    val nombreCuenta: String,
    val tipo: TipoMovimiento

)