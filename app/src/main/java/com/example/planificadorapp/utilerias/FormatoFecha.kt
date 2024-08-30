package com.example.planificadorapp.utilerias

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Objeto que contiene funciones para formatear fechas
 */
object FormatoFecha {

    /**
     * Formatea una fecha en formato de cadena con el patrón yyyy-MM-dd
     */
    fun formato(fecha: LocalDateTime):String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fecha.format(formatter)
    }

    /**
     * Formatea una fecha en formato de cadena con el patrón dd 'de' MMMM 'de' yyyy
     */
    fun formatoLargo(fecha:LocalDateTime):String{
        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))
        return fecha.format(formatter)
    }
}