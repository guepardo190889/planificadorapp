package com.example.planificadorapp.utilerias

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
     * Convierte un monto formateado a Double
     */
    fun convertirADouble(monto: String): Double {
        return monto.replace("$", "").replace(",", "").replace(" ", "").toDouble()
    }
}

