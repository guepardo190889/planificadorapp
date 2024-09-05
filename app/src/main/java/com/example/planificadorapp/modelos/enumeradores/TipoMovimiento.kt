package com.example.planificadorapp.modelos.enumeradores

/**
 * Enumerador de tipos de movimientos
 */
enum class TipoMovimiento(
    val id: Long
) {
    CARGO(1),
    ABONO(2)
}