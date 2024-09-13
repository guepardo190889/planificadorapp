package com.example.planificadorapp.modelos.movimientos

import com.example.planificadorapp.utilerias.enumeradores.TipoMovimiento
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
    val tipo: TipoMovimiento

)