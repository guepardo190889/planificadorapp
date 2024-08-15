package com.example.planificadorapp.repositorios

import android.util.Log
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CuentaRepository {
    private val apiService = ApiClient.apiService

    fun guardarCuenta(cuenta: CuentaModel, onResult: (CuentaModel?) -> Unit) {
        val call = ApiClient.apiService.guardarCuenta(cuenta)
        call.enqueue(object : Callback<CuentaModel> {
            override fun onResponse(call: Call<CuentaModel>, response: Response<CuentaModel>) {
                Log.i("CuentaRepository", "Respuesta del servidor: ${response.body()}")
                if (response.isSuccessful) {
                    onResult(response.body())
                    println("Cuenta guardada exitosamente")
                } else {
                    onResult(null)
                    println("Error al guardar la cuenta")
                }
            }

            override fun onFailure(call: Call<CuentaModel>, t: Throwable) {
                onResult(null)
                println("Fallo en la conexión: ${t.message}")
            }
        })
    }

    fun actualizarCuenta(cuenta: CuentaModel, callback: (CuentaModel?) -> Unit) {
        val idCuenta: Long? = cuenta.id

        idCuenta?.let {
            val call = apiService.actualizarCuenta(idCuenta, cuenta)
            call.enqueue(object : Callback<CuentaModel> {
                override fun onResponse(call: Call<CuentaModel>, response: Response<CuentaModel>) {
                    if (response.isSuccessful) {
                        callback(response.body())
                    } else {
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<CuentaModel>, t: Throwable) {
                    callback(null)
                }
            })
        }
    }

    fun buscarCuentaPorId(id: Long, onResult: (CuentaModel?) -> Unit) {
        val call = ApiClient.apiService.obtenerCuentaPorId(id)
        call.enqueue(object : Callback<CuentaModel> {
            override fun onResponse(call: Call<CuentaModel>, response: Response<CuentaModel>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<CuentaModel>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun buscarCuentas(onResult: (List<CuentaModel>?) -> Unit) {
        apiService.buscarCuentas().enqueue(object : Callback<List<CuentaModel>> {
            override fun onResponse(
                call: Call<List<CuentaModel>>,
                response: Response<List<CuentaModel>>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<CuentaModel>>, t: Throwable) {
                onResult(null)
            }
        })
    }
}