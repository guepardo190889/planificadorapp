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
import androidx.compose.ui.text.style.TextAlign
import com.example.planificadorapp.utilerias.FormatoMonto
import java.math.BigDecimal
import java.math.RoundingMode

const val MAX_ENTEROS = 9

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
                val montoString: String

                val partes = monto
                    .setScale(2, RoundingMode.DOWN)
                    .toPlainString()
                    .split(".")

                val enteros = partes[0]
                val decimales = partes[1]

                when (event.nativeKeyEvent.keyCode) {
                    // Capturamos teclas numéricas
                    KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4,
                    KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_9 -> {
                        val numeroPresionado = event.nativeKeyEvent.keyCode - KeyEvent.KEYCODE_0

                        if (isCapturaEntera) {
                            if (enteros.length < MAX_ENTEROS) {
                                montoString = enteros + numeroPresionado.toString()
                                monto = BigDecimal(montoString)
                            }
                        } else {
                            if (decimales.trimEnd('0').length < 2) {
                                if (decimales
                                        .trimEnd('0')
                                        .isEmpty()
                                ) {
                                    montoString = "$enteros.$numeroPresionado"
                                    monto = BigDecimal(montoString)
                                } else if (decimales.trimEnd('0').length == 1) {
                                    montoString =
                                        enteros + "." + decimales.dropLast(1) + numeroPresionado.toString()
                                    monto = BigDecimal(montoString)
                                }
                            }
                        }

                        onSaldoChange(monto)

                        true
                    }

                    KeyEvent.KEYCODE_PERIOD -> {
                        isCapturaEntera = false

                        true
                    }

                    KeyEvent.KEYCODE_DEL -> {
                        if (isCapturaEntera) {
                            montoString = enteros.dropLast(1)
                        } else {
                            if (decimales
                                    .trimEnd('0')
                                    .isEmpty()
                            ) {
                                isCapturaEntera = true
                                montoString = enteros.dropLast(1)
                            } else if (decimales.trimEnd('0').length == 1) {
                                montoString = enteros
                            } else {
                                montoString = enteros + "." + decimales.dropLast(1)
                            }
                        }

                        monto = if (montoString.isEmpty()) {
                            BigDecimal.ZERO
                        } else {
                            BigDecimal(montoString)
                        }

                        onSaldoChange(monto)

                        true
                    }

                    else -> true
                }
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