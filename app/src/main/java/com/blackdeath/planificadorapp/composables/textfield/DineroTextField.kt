package com.blackdeath.planificadorapp.composables.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.blackdeath.planificadorapp.utilerias.FormatoMonto

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
    monto: String = "",
    isError: Boolean = false,
    isNegativo: Boolean = false,
    focusRequester: FocusRequester? = null,
    onSaldoChange: (String) -> Unit,
    onNextAction: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    var textFieldValue by remember(monto) {
        val formattedMonto = if (monto.isNotEmpty() && monto != "0.00") {
            FormatoMonto.agregarSeparadoresMiles(monto.replace(",", ""))
        } else {
            monto
        }
        mutableStateOf(TextFieldValue(formattedMonto, TextRange(formattedMonto.length)))
    }

    /**
     * Valida si un texto es válido para el saldo monetario
     */
    fun isTextoValido(texto: String): Boolean {
        var contadorEnteros = 0
        var contadorDecimales = 0
        var contadorPuntos = 0
        var isConPunto = false

        for (caracter in texto) {
            if (caracter == '.') {
                contadorPuntos++
                isConPunto = true
            } else if (caracter.isDigit()) {
                if (isConPunto) {
                    contadorDecimales++
                } else {
                    contadorEnteros++
                }
            } else {
                return false // Caracter no permitido
            }

            if (contadorEnteros > MAX_ENTEROS || contadorPuntos > 1 || contadorDecimales > MAX_DECIMALES) {
                return false
            }
        }
        return true
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (focusRequester != null) Modifier.focusRequester(focusRequester)
                else Modifier
            ),
        value = textFieldValue,
        onValueChange = { newValue ->
            val textoSinComas = newValue.text.replace(",", "")

            if (isTextoValido(textoSinComas)) {
                val textoFormateado = FormatoMonto.agregarSeparadoresMiles(textoSinComas)
                textFieldValue = TextFieldValue(
                    text = textoFormateado,
                    selection = TextRange(textoFormateado.length)  // Mueve el cursor al final
                )
                onSaldoChange(textoFormateado)
            } else {
                // Mantener el cursor al final si el texto no es válido
                textFieldValue =
                    textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
            }
        },
        label = { Text(etiqueta) },
        placeholder = { Text("0.00") },
        leadingIcon = { Text(if (isNegativo) "$ -" else "$") },
        isError = isError,
        textStyle = MaterialTheme.typography.bodyLarge,
        supportingText = {
            if (isError && mensajeError != null) {
                Text(
                    text = mensajeError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
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
        }),
    )
}