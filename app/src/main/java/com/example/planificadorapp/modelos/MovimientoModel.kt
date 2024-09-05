package com.example.planificadorapp.modelos

import com.example.planificadorapp.modelos.enumeradores.TipoMovimiento
import java.time.LocalDateTime

/**
 * Modelo de datos para representar un movimiento
 */
data class MovimientoModel (
    var id: Long = 0,
    val descripcion: String,
    val monto: Double,
    val fecha: LocalDateTime,
    val idCuenta: Long = 0,
    val tipo: TipoMovimiento

)