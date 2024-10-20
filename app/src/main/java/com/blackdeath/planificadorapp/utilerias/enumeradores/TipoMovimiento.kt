package com.blackdeath.planificadorapp.utilerias.enumeradores

/**
 * Enumerador de tipos de movimientos
 */
enum class TipoMovimiento(
    val id: Long
) {
    CARGO(1),
    ABONO(2),
    AJUSTE_CARGO(3),
    AJUSTE_ABONO(4)
}