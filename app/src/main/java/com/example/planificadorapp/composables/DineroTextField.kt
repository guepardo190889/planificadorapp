package com.example.planificadorapp.composables

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.planificadorapp.utilerias.FormatoMonto
import java.math.BigDecimal
import java.math.RoundingMode

const val MAX_ENTEROS = 9
const val MAX_DECIMALES = 2

/**
 * Composable que representa un campo de entrada de texto para el saldo monetario
 */
@Composable
fun DineroTextField(
    modifier: Modifier,
    saldoInicial: BigDecimal?,
    isSaldoValido: Boolean,
    onSaldoChange: (BigDecimal) -> Unit
) {
    var isCapturaEntera by remember { mutableStateOf(true) }
    var monto by remember { mutableStateOf(saldoInicial ?: BigDecimal.ZERO) }

    // Actualizar 'monto' si 'saldoInicial' cambia
    LaunchedEffect (saldoInicial) {
        saldoInicial?.let {
            monto = it

            // Determinar si la captura es entera o decimal
            isCapturaEntera = monto.remainder(BigDecimal.ONE) == BigDecimal.ZERO
        }
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
                monto = calcularMonto(event.nativeKeyEvent.keyCode, monto, isCapturaEntera)

                onSaldoChange(monto)

                // Cambiar el estado de captura según la tecla presionada
                if (event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_PERIOD) {
                    isCapturaEntera = false
                }

                true
            },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        supportingText = {
            if (!isSaldoValido) {
                Text(
                    text = "El saldo es requerido",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors()
    )
}

/**
 * Calcula el monto a partir de la tecla presionada
 */
private fun calcularMonto(keyCode: Int, monto: BigDecimal, isCapturaEntera: Boolean): BigDecimal {
    val montoString: String
    val partes = monto.setScale(2, RoundingMode.DOWN).toPlainString().split(".")
    val enteros = partes[0]
    val decimales = partes[1].trimEnd('0')

    when (keyCode) {
        in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> {
            val numeroPresionado = keyCode - KeyEvent.KEYCODE_0
            montoString = if (isCapturaEntera) {
                if (enteros.length < MAX_ENTEROS) enteros + numeroPresionado else enteros
            } else {
                if (decimales.length < MAX_DECIMALES) "$enteros.$decimales$numeroPresionado" else "$enteros.$decimales"
            }
            return BigDecimal(montoString)
        }
        KeyEvent.KEYCODE_DEL -> {
            montoString = if (isCapturaEntera) {
                enteros.dropLast(1).ifEmpty { "0" }
            } else {
                when {
                    decimales.isEmpty() -> enteros.dropLast(1).ifEmpty { "0" }
                    decimales.length == 1 -> enteros
                    else -> "$enteros.${decimales.dropLast(1)}"
                }
            }
            return BigDecimal(montoString.ifEmpty { "0" })
        }
        else -> return monto
    }
}