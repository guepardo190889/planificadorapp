package com.example.planificadorapp.repositorios

import android.util.Log
import com.example.planificadorapp.modelos.Cuenta
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CuentaRepository {
    private val apiService = ApiClient.apiService

    fun guardarCuenta(cuenta: Cuenta, onResult: (Cuenta?) -> Unit) {
        val call = ApiClient.apiService.guardarCuenta(cuenta)
        call.enqueue(object : Callback<Cuenta> {
            override fun onResponse(call: Call<Cuenta>, response: Response<Cuenta>) {
                Log.i("CuentaRepository", "Respuesta del servidor: ${response.body()}")
                if (response.isSuccessful) {
                    onResult(response.body())
                    println("Cuenta guardada exitosamente")
                } else {
                    onResult(null)
                    println("Error al guardar la cuenta")
                }
            }

            override fun onFailure(call: Call<Cuenta>, t: Throwable) {
                onResult(null)
                println("Fallo en la conexión: ${t.message}")
            }
        })
    }

    fun buscarCuentas(onResult: (List<Cuenta>?) -> Unit) {
        apiService.buscarCuentas().enqueue(object : Callback<List<Cuenta>> {
            override fun onResponse(call: Call<List<Cuenta>>, response: Response<List<Cuenta>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<Cuenta>>, t: Throwable) {
                onResult(null)
            }
        })
    }
}