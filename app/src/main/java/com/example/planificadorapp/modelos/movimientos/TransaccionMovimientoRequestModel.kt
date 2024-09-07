package com.example.planificadorapp.modelos.movimientos

import java.time.LocalDate

/**
 * Modelo de datos para guardar o actualizar un movimiento
 */
data class TransaccionMovimientoRequestModel(
    var monto: Double,
    var concepto: String?,
    var descripcion: String?,
    var fecha: LocalDate,
    var idCuenta: Long,
    var tipo: String

)