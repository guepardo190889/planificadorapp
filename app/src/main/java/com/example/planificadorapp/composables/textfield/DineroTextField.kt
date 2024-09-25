package com.example.planificadorapp.composables.textfield

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
    etiqueta: String,
    mensajeError: String? = null,
    saldoInicial: BigDecimal? = BigDecimal.ZERO,
    isSaldoValido: Boolean = true,
    focusRequester: FocusRequester? = null,
    onSaldoChange: (BigDecimal) -> Unit,
    onNextAction: (() -> Unit)? = null
) {
    var isCapturaEntera by remember { mutableStateOf(true) }
    var monto by remember { mutableStateOf(saldoInicial ?: BigDecimal.ZERO) }
    val focusManager = LocalFocusManager.current

    // Actualizar 'monto' si 'saldoInicial' cambia
    LaunchedEffect(saldoInicial) {
        saldoInicial?.let {
            monto = it

            // Determinar si la captura es entera o decimal
            isCapturaEntera = monto.remainder(BigDecimal.ONE) == BigDecimal.ZERO
        }
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (focusRequester != null) Modifier.focusRequester(focusRequester)
                else Modifier
            )
            .onKeyEvent { event ->
                Log.i("DineroTextField", "KeyCode: ${event.nativeKeyEvent.keyCode}")

                monto = calcularMonto(event.nativeKeyEvent.keyCode, monto, isCapturaEntera)

                onSaldoChange(monto)

                // Cambiar el estado de captura según la tecla presionada
                if (event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_PERIOD || event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_NUMPAD_DOT) {
                    isCapturaEntera = false
                }

                true
            },
        value = FormatoMonto.formatoSinSimbolo(monto),
        onValueChange = { /* Ignoramos el cambio de valor aquí */ },
        label = { Text(etiqueta) },
        leadingIcon = { Text("$") },
        isError = !isSaldoValido,
        textStyle = MaterialTheme.typography.bodyLarge,
        supportingText = {
            if (!isSaldoValido && mensajeError != null) {
                Text(
                    text = mensajeError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (onNextAction != null) ImeAction.Next else ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            if (onNextAction == null) {
                focusManager.clearFocus()
            }
        }, onNext = {
            if (onNextAction != null) {
                onNextAction()
            }
        })
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