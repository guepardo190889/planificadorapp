package com.example.planificadorapp.utilerias

/**
 * Objeto que contiene funciones para manipular cadenas
 */
object Cadena {
    /**
     * Capitaliza la primera letra de un texto
     */
    fun capitalizarPrimeraLetra(texto: String): String {
        return texto.lowercase().replaceFirstChar { it.uppercase() }
    }
}