package com.example.planificadorapp.repositorios

import android.util.Log
import com.example.planificadorapp.modelos.Cuenta
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CuentaRepository {
    private val apiService = ApiClient.apiService

    fun guardarCuenta(cuenta: Cuenta) {
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

    fun buscarCuentas(onResult: (List<Cuenta>?) -> Unit) {
        Log.d("CuentaRepository", "Making API call to get cuentas")
        apiService.buscarCuentas().enqueue(object : Callback<List<Cuenta>> {
            override fun onResponse(call: Call<List<Cuenta>>, response: Response<List<Cuenta>>) {
                if (response.isSuccessful) {
                    Log.d("CuentaRepository", "API call successful: ${response.body()?.size} cuentas fetched")
                    onResult(response.body())
                } else {
                    Log.e("CuentaRepository", "API call failed with response code ${response.code()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<Cuenta>>, t: Throwable) {
                Log.e("CuentaRepository", "API call failed: ${t.message}")
                onResult(null)
            }
        })
    }
}