package com.blackdeath.planificadorapp.utilerias

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/**
 * Objeto que contiene funciones para formatear montos
 */
object FormatoMonto {

    /**
     * Formatea un monto en formato de moneda para México
     */
    fun formato(amount: Double): String {
        val formato: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        formato.maximumFractionDigits = 2
        formato.minimumFractionDigits = 2
        return formato.format(amount)
    }

    /**
     * Formatea un monto en formato de moneda para México
     */
    fun formato(cantidad: BigDecimal): String {
        val formato: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        formato.maximumFractionDigits = 2
        formato.minimumFractionDigits = 2
        return formato.format(cantidad)
    }

    /**
     * Agrega separadores de miles a un número
     */
    fun agregarSeparadoresMiles(numero: String): String {
        // Divide el número en parte entera y decimal
        val partes = numero.split(".")
        val parteEntera = partes[0]

        // Formatear la parte entera con comas
        val parteEnteraConComas = parteEntera.reversed().chunked(3).joinToString(",").reversed()

        // Si existe una parte decimal, agregarla al final
        return if (partes.size > 1) {
            "$parteEnteraConComas.${partes[1]}"
        } else {
            parteEnteraConComas
        }
    }
}

