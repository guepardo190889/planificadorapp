package com.example.planificadorapp.repositorios

import android.util.Log
import com.example.planificadorapp.modelos.cuentas.ActualizarCuentaRequestModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.cuentas.GuardarCuentaRequestModel
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositorio para interactuar con el servicio de cuentas
 */
class CuentasRepository {
    private val apiService = ApiClient.cuentaService

    /**
     * Guarda una cuenta en el servidor y devuelve la cuenta guardada
     */
    fun guardarCuenta(
        cuenta: GuardarCuentaRequestModel,
        onResult: (CuentaModel?) -> Unit
    ) {
        val call = apiService.guardarCuenta(cuenta)
        call.enqueue(object :
            Callback<CuentaModel> {
            override fun onResponse(
                call: Call<CuentaModel>,
                response: Response<CuentaModel>
            ) {
                Log.i("CuentaRepository", "Respuesta del servidor: ${response.body()}")
                if (response.isSuccessful) {
                    onResult(response.body())
                    println("Cuenta guardada exitosamente")
                } else {
                    onResult(null)
                    println("Error al guardar la cuenta")
                }
            }

            override fun onFailure(
                call: Call<CuentaModel>,
                t: Throwable
            ) {
                Log.e("CuentasRepository", "Error al guardar la cuenta", t)
                onResult(null)
            }
        })
    }

    /**
     * Actualiza una cuenta en el servidor y devuelve la cuenta actualizada
     */
    fun actualizarCuenta(
        id: Long,
        cuenta: ActualizarCuentaRequestModel,
        callback: (CuentaModel?) -> Unit
    ) {
        val call = apiService.actualizarCuenta(id, cuenta)
        call.enqueue(object :
            Callback<CuentaModel> {
            override fun onResponse(
                call: Call<CuentaModel>,
                response: Response<CuentaModel>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(
                call: Call<CuentaModel>,
                t: Throwable
            ) {
                Log.e("CuentasRepository", "Error al actualizar la cuenta", t)
                callback(null)
            }
        })
    }

    /**
     * Busca una cuenta por su ID en el servidor y devuelve la cuenta encontrada
     */
    fun buscarCuentaPorId(
        id: Long,
        onResult: (CuentaModel?) -> Unit
    ) {
        val call = apiService.obtenerCuentaPorId(id)
        call.enqueue(object :
            Callback<CuentaModel> {
            override fun onResponse(
                call: Call<CuentaModel>,
                response: Response<CuentaModel>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(
                call: Call<CuentaModel>,
                t: Throwable
            ) {
                Log.e("CuentasRepository", "Error al buscar la cuenta por id", t)
                onResult(null)
            }
        })
    }

    /**
     * Busca todas las cuentas en el servidor y devuelve una lista de cuentas encontradas
     */
    fun buscarCuentas(
        excluirCuentasAsociadas: Boolean,
        incluirSoloCuentasNoAgrupadorasSinAgrupar: Boolean,
        onResult: (List<CuentaModel>?) -> Unit
    ) {
        apiService.buscarCuentas(excluirCuentasAsociadas, incluirSoloCuentasNoAgrupadorasSinAgrupar)
            .enqueue(object :
                Callback<List<CuentaModel>> {
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

                override fun onFailure(
                    call: Call<List<CuentaModel>>,
                    t: Throwable
                ) {
                    Log.e("CuentasRepository", "Error al buscar las cuentas", t)
                    onResult(null)
                }
            })
    }

    /**
     * Busca las subcuentas de una cuenta en el servidor y devuelve una lista de subcuentas encontradas
     */
    fun buscarSubcuentas(
        idCuentaPadre: Long,
        onResult: (List<CuentaModel>?) -> Unit
    ) {
        apiService.buscarSubcuentas(idCuentaPadre)
            .enqueue(object :
                Callback<List<CuentaModel>> {
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

                override fun onFailure(
                    call: Call<List<CuentaModel>>,
                    t: Throwable
                ) {
                    Log.e("CuentasRepository", "Error al buscar las subcuentas", t)
                    onResult(null)
                }
            })
    }
}