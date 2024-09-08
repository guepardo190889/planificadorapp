package com.example.planificadorapp.pantallas.cuentas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.planificadorapp.modelos.ValidacionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import java.util.regex.Pattern

@Composable
fun ActualizarCuentaDialog(
    cuenta: CuentaModel,
    onDismiss: () -> Unit,
    onUpdate: (CuentaModel) -> Unit
) {
    var nombre by remember { mutableStateOf(cuenta.nombre) }
    var descripcion by remember { mutableStateOf(cuenta.descripcion) }
    var saldo by remember { mutableStateOf(cuenta.saldo.toString()) }

    var validacionNombre by remember { mutableStateOf(ValidacionModel()) }
    var validacionDescripcion by remember { mutableStateOf(ValidacionModel()) }
    var validacionSaldo by remember { mutableStateOf(ValidacionModel()) }

    val nombrePattern = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-&,]{1,32}\$")
    val descripcionPattern = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-&,]{1,256}\$")

    fun validarNombre(): ValidacionModel {
        return if (nombre.isBlank()) {
            ValidacionModel(false, "El nombre es requerido")
        } else if (!nombrePattern.matcher(nombre).matches()) {
            ValidacionModel(
                false,
                "El nombre solo puede contener letras, espacios, acentos y números, hasta 32 caracteres"
            )
        } else {
            ValidacionModel(true)
        }
    }

    fun validarDescripcion(): ValidacionModel {
        return if (descripcion.isBlank()) {
            ValidacionModel(false, "La descripción es requerida")
        } else if (!descripcionPattern.matcher(descripcion).matches()) {
            ValidacionModel(
                false,
                "La descripción solo puede contener letras, espacios y acentos, hasta 128 caracteres"
            )
        } else {
            ValidacionModel(true)
        }
    }

    fun validarSaldo(): ValidacionModel {
        return try {
            val saldoValue = saldo.toDouble()
            if (saldoValue < 0 || saldoValue > 999_999_999.0) {
                ValidacionModel(false, "El saldo debe estar entre 0 y 999,999,999.00")
            } else {
                ValidacionModel(true)
            }
        } catch (e: NumberFormatException) {
            return ValidacionModel(false, "El saldo debe ser un número válido")
        }
    }

    fun validarDialogo(): Boolean {
        validacionNombre = validarNombre()
        validacionDescripcion = validarDescripcion()
        validacionSaldo = validarSaldo()
        return validacionNombre.isValid && validacionDescripcion.isValid && validacionSaldo.isValid
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Actualizar Cuenta", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        if (it.length <= 32) {
                            nombre = it
                        }
                    },
                    label = { Text("Nombre") },
                    isError = !validacionNombre.isValid,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 2
                )
                if (!validacionNombre.isValid) {
                    Text(
                        text = validacionNombre.mensaje!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        if (it.length <= 128) {
                            descripcion = it
                        }
                    },
                    label = { Text("Descripción") },
                    isError = !validacionDescripcion.isValid,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 3
                )
                if (!validacionDescripcion.isValid) {
                    Text(
                        text = validacionDescripcion.mensaje!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = saldo,
                    onValueChange = { saldo = it },
                    label = { Text("Saldo") },
                    isError = !validacionSaldo.isValid,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                if (!validacionSaldo.isValid) {
                    Text(
                        text = validacionSaldo.mensaje!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (validarDialogo()) {
                                val cuentaActualizada = cuenta.copy(
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    saldo = saldo.toDouble()
                                )
                                onUpdate(cuentaActualizada)
                            }
                        }
                    ) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}