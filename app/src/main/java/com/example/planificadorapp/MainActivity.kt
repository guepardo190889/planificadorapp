package com.example.planificadorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.Cuenta
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                MyScreenContent()
            }
        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

@Composable
fun MyScreenContent() {
    var nombre by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = saldo,
            onValueChange = { saldo = it },
            label = { Text("Saldo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { saveCuenta(Cuenta(nombre, saldo.toDouble())) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}

fun saveCuenta(cuenta: Cuenta) {
    val call = ApiClient.apiService.guardarCuenta(cuenta)
    call.enqueue(object : Callback<Cuenta> {
        override fun onResponse(call: Call<Cuenta>, response: Response<Cuenta>) {
            if (response.isSuccessful) {
                println("Cuenta guardada exitosamente")
            } else {
                println("Error al guardar la cuenta")
            }
        }

        override fun onFailure(call: Call<Cuenta>, t: Throwable) {
            println("Fallo en la conexión: ${t.message}")
        }
    })
}