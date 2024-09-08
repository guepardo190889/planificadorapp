package com.example.planificadorapp.utilerias

import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel

/**
 * Objeto que contiene funciones para calcular valores
 */
object Calculo {

    /**
     * Suma los porcentajes de una lista de composiciones
     */
    fun sumarPorcentajes(activos: List<GuardarComposicionModel>): Int {
        return activos.sumOf { it.porcentaje.toDouble() }.toInt()
    }
}