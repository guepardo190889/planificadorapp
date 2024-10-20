package com.blackdeath.planificadorapp.utilerias.validadores

/**
 * Validador con funciones para validar un portafolio
 */
object PortafolioValidador {

    /**
     * Valida el nombre de un portafolio
     */
    fun validarNombre(nombre: String): Boolean {
        return nombre.isNotBlank()
    }
}