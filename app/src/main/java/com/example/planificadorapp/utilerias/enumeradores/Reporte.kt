package com.example.planificadorapp.utilerias.enumeradores

import com.example.planificadorapp.navegacion.Ruta

/**
 * Enumerador de reportes
 */
enum class Reporte(
    val id:Int,
    val ruta: Ruta
) {
    PORTAFOLIOS_DISTRIBUCION_ACTIVOS(1, Ruta.REPORTES_PORTAFOLIO_DISTRIBUCION_ACTIVOS),
    PORTAFOLIOS_DISTRIBUCION_SALDOS(2, Ruta.REPORTES_PORTAFOLIO_DISTRIBUCION_SALDOS),

    CUENTAS_HISTORICO_SALDO(6, Ruta.REPORTES_CUENTAS_HISTORICO_SALDO);

    /**
     * Funciones estáticas para obtener reportes
     */
    companion object {
        /**
         * Obtiene la ruta correspondiente a un reporte por su ID
         */

        fun obtenerRutaPorId(id: Int): Ruta? {
            return entries.find { it.id == id }?.ruta
        }

        /**
         * Obtiene un reporte por su ID
         */
        fun obtenerPorId(id: Int): Reporte? {
            return entries.find { it.id == id }
        }
    }
}