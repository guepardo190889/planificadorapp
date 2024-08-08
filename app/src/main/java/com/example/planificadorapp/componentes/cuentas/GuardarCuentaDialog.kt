package com.example.planificadorapp.componentes.cuentas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.planificadorapp.modelos.Cuenta
import com.example.planificadorapp.modelos.Validacion
import com.example.planificadorapp.repositorios.CuentaRepository
import java.util.regex.Pattern

@Composable
fun GuardarCuentaDialog(onDismiss: () -> Unit, onSave: (Cuenta) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf("") }

    var validacionNombre by remember { mutableStateOf(Validacion()) }
    var validacionDescripcion by remember { mutableStateOf(Validacion()) }
    var validacionSaldo by remember { mutableStateOf(Validacion()) }

    val nombrePattern = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-&,]{1,32}\$")
    val descripcionPattern = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-&,]{1,256}\$")

    fun validarNombre(): Validacion {
        return if (nombre.isBlank()) {
            Validacion(false, "El nombre es requerido")
        } else if (!nombrePattern.matcher(nombre).matches()) {
            Validacion(false, "El nombre solo puede contener letras, espacios, acentos y números, hasta 32 caracteres")
        } else {
            Validacion(true)
        }
    }

    fun validarDescripcion(): Validacion {
        return if (descripcion.isBlank()) {
            Validacion(false, "La descripción es requerida")
        } else if (!descripcionPattern.matcher(descripcion).matches()) {
            Validacion(false, "La descripción solo puede contener letras, espacios y acentos, hasta 128 caracteres")
        } else {
            Validacion(true)
        }
    }

    fun validarSaldo(): Validacion {
        return try {
            val saldoValue = saldo.toDouble()
            if (saldoValue < 0 || saldoValue > 999_999_999.0) {
                Validacion(false, "El saldo debe estar entre 0 y 999,999,999.00")
            } else {
                Validacion(true)
            }
        } catch (e: NumberFormatException) {
            return Validacion(false, "El saldo debe ser un número válido")
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
                Text(text = "Guardar Cuenta", style = MaterialTheme.typography.headlineSmall)
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
                                val nuevaCuenta = Cuenta(
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    saldo = saldo.toDouble()
                                )
                                onSave(nuevaCuenta)
                            }
                        },
                        //enabled = validacionNombre.isValid && validacionDescripcion.isValid && validacionSaldo.isValid
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

