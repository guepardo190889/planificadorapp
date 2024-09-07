package com.example.planificadorapp.utilerias

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Objeto que contiene funciones para formatear fechas
 */
object FormatoFecha {

    /**
     * Formatea una fecha en formato de cadena con el patrón yyyy-MM-dd
     */
    fun formato(fecha: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fecha.format(formatter)
    }

    /**
     * Formatea una fecha en formato de cadena con el patrón dd 'de' MMMM 'de' yyyy
     */
    fun formatoLargo(fecha: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))
        return fecha.format(formatter)
    }

    /**
     * Formatea una fecha en formato de cadena con el patrón yyyy-MM-dd hh:mm:ss
     */
    fun formatoCortoConHora(fecha: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss", Locale("es", "MX"))
        return fecha.format(formatter)
    }

    /**
     * Convierte LocalDate a milisegundos
     */
    fun convertirLocalDateAMilisegundos(fecha: LocalDate): Long {
        return fecha.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    /**
     * Convierte milisegundos a LocalDate
     */
    fun convertirMilisegundosALocalDate(milisegundos: Long): LocalDate {
        val instant = Instant.ofEpochMilli(milisegundos)
        return instant.atZone(ZoneOffset.UTC).toLocalDate()
    }

    /**
     * Formatea una fecha en formato de cadena con el patrón MM-dd-yyyy
     */
    fun formatoCorto(fecha: LocalDate): String {
        // Crear un DateTimeFormatter con el formato MM-dd-yyyy
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")

        // Convertir el LocalDate a String
        return fecha.format(formatter)
    }

    /**
     * Formatea una fecha en formato de cadena con el patrón yyyy-MM-dd de acuerdo a ISO 8601
     */
    fun formatoCortoISO8601(fecha: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return formatter.format(fecha)
    }

}