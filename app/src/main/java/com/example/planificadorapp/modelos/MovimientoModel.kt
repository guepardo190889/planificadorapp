package com.example.planificadorapp.modelos

import com.example.planificadorapp.utilerias.enumeradores.TipoMovimiento
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Modelo de datos para representar un movimiento
 */
data class MovimientoModel (
    var id: Long = 0,
    val descripcion: String,
    val monto: Double,
    val fecha: LocalDate,
    val idCuenta: Long = 0,
    val tipo: TipoMovimiento

)