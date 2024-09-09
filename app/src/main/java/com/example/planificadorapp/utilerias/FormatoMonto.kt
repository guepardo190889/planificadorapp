package com.example.planificadorapp.utilerias

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

/**
 * Objeto que contiene funciones para formatear montos
 */
object FormatoMonto {

    /**
     * Formatea un monto en Bigdecimal al formato #,##0.00
     */
    fun formatoBigDecimal(monto:BigDecimal): String{
        val decimalFormat = DecimalFormat("#,##0.00")
        return decimalFormat.format(monto)
    }

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
    fun formato (cantidad: BigDecimal): String {
        val formato: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        formato.maximumFractionDigits = 2
        formato.minimumFractionDigits = 2
        return formato.format(cantidad)
    }

    /**
     * Formatea un monto en formato de moneda para México sin el símbolo de moneda a la izquierda
     */
    fun formatoSinSimbolo(cantidad: BigDecimal): String {
        val symbols = DecimalFormatSymbols(Locale("es", "MX"))
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','

        val formato = DecimalFormat("#,##0.00", symbols)
        return formato.format(cantidad)
    }

    /**
     * Convierte un monto formateado a Double
     */
    fun convertirADouble(monto: String): Double {
        return monto.replace("$", "").replace(",", "").replace(" ", "").toDouble()
    }
}

