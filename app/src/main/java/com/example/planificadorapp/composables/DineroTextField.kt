package com.example.planificadorapp.composables

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.KeyboardType
import com.example.planificadorapp.utilerias.FormatoMonto
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun MonetaryInputField(
    modifier: Modifier,
    saldoInicial: BigDecimal?,
    isSaldoValido: Boolean,
    onSaldoChange: (BigDecimal) -> Unit
) {
    val MAXIMO_CAPTURAS = 11

    /**
     * Calcula un texto de solo números considerando hasta 9 enteros y 2 decimales a partir de un BigDecimal
     */
    fun calcularCapturaUsuarioDesdeBigDecimal(monto: BigDecimal?): String {
        var capturaUsuarioCalculada = ""

        if (monto != null && monto > BigDecimal.ZERO) {
            // Formatear el BigDecimal a un formato consistente con dos decimales
            val montoFormateado = monto.setScale(2, RoundingMode.DOWN).toPlainString()
            val partes = montoFormateado.split(".")
            val enteros = partes[0] // Parte entera del número
            val decimales = partes[1] // Parte decimal del número

            // Concatenar enteros y decimales según las reglas de la tabla
            capturaUsuarioCalculada = when {
                // Si es un número de un solo dígito y su decimal es 0, mostrar solo el entero
                enteros.length == 1 && decimales == "00" -> enteros
                // Si es un número con un decimal diferente de 0, concatenar los enteros y el primer decimal
                enteros.length == 1 && decimales != "00" -> enteros + decimales
                // Para números con dos enteros (como 99.99)
                enteros.length == 2 -> enteros + decimales
                // Para números con tres enteros (como 999.99)
                enteros.length == 3 -> enteros + decimales
                // Para números con más de 3 dígitos en la parte entera
                else -> enteros + decimales
            }
        }

        return capturaUsuarioCalculada
    }

    var capturaUsuario by remember { mutableStateOf(calcularCapturaUsuarioDesdeBigDecimal(saldoInicial)) }
    var monto by remember { mutableStateOf(BigDecimal.ZERO) }

    /**
     * Calcula un BigDecimal a partir de una cadena de texto de solo números considerando hasta 9 enteros y 2 decimales
     */
    fun calcularBigDecimal(capturaUsuario:String):BigDecimal{
        var saldoBigDecimal: BigDecimal = BigDecimal.ZERO

        if (capturaUsuario.isEmpty()) {
            saldoBigDecimal = BigDecimal.ZERO
        } else if (capturaUsuario.length == 1) {
            saldoBigDecimal = BigDecimal(capturaUsuario)
        } else if (capturaUsuario.length == 2) {
            saldoBigDecimal = BigDecimal(capturaUsuario.substring(0, 1) + "." + capturaUsuario.substring(1, 2))
        } else if (capturaUsuario.length == 3) {
            saldoBigDecimal = BigDecimal(capturaUsuario.substring(0, 1) + "." + capturaUsuario.substring(1, 3))
        } else if (capturaUsuario.length == 4) {
            saldoBigDecimal = BigDecimal(capturaUsuario.substring(0, 2) + "." + capturaUsuario.substring(2, 4))
        } else {
            saldoBigDecimal = BigDecimal(capturaUsuario.substring(0, capturaUsuario.length - 2) + "." + capturaUsuario.substring(capturaUsuario.length - 2))
        }

        return saldoBigDecimal
    }

    OutlinedTextField(
        value = FormatoMonto.formatoSinSimbolo(monto),
        onValueChange = { /* Ignoramos el cambio de valor aquí */ },
        label = { Text("Saldo") },
        leadingIcon = { Text("$") },
        isError = !isSaldoValido,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .fillMaxWidth()
            .onKeyEvent { event ->
                 when (event.nativeKeyEvent.keyCode) {
                    // Capturamos teclas numéricas
                    KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4,
                    KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_9 -> {
                        val numeroPresionado = event.nativeKeyEvent.keyCode - KeyEvent.KEYCODE_0

                        if(capturaUsuario.length < MAXIMO_CAPTURAS){
                            capturaUsuario = numeroPresionado.toString() + capturaUsuario

                            monto = calcularBigDecimal(capturaUsuario)
                        }

                        onSaldoChange(monto)

                        true
                    }
                    KeyEvent.KEYCODE_DEL -> {
                        if(capturaUsuario.isNotEmpty()){
                            if(capturaUsuario.length == 1) {
                                capturaUsuario = ""
                            }
                            else {
                                capturaUsuario = capturaUsuario.substring(1, capturaUsuario.length)
                            }

                            monto = calcularBigDecimal(capturaUsuario)
                        }

                        onSaldoChange(monto)

                        true
                    }
                    else -> true
                }
            },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors()
    )
}