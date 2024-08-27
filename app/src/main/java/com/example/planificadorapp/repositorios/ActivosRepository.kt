package com.example.planificadorapp.repositorios

import android.util.Log
import com.example.planificadorapp.modelos.ActivoGuardarRequestModel
import com.example.planificadorapp.modelos.ActivoModel
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositorio para interactuar con el servicio de activos
 */
class ActivosRepository {
    private val apiService = ApiClient.activosService

    /**
     * Guarda un activo en el servidor y devuelve el activo guardado
     */
    fun guardarActivo(activo: ActivoGuardarRequestModel, onResult: (ActivoModel?) -> Unit) {
        val call = apiService.guardarActivo(activo)
        call.enqueue(object : Callback<ActivoModel> {
            override fun onResponse(call: Call<ActivoModel>, response: Response<ActivoModel>) {
                Log.i("ActivosRepository", "Respuesta del servidor: ${response.body()}")
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ActivoModel>, t: Throwable) {
                onResult(null)
            }
        })
    }

    /**
     * Busca los activos en el servidor y devuelve una lista de ActivoModel.
     */
    fun buscarActivos(incluirSoloActivosPadre: Boolean, callback: (List<ActivoModel>?) -> Unit) {
        apiService.buscarActivos(incluirSoloActivosPadre)
            .enqueue(object : Callback<List<ActivoModel>> {
                override fun onResponse(
                    call: Call<List<ActivoModel>>,
                    response: Response<List<ActivoModel>>
                ) {
                    if (response.isSuccessful) {
                        callback(response.body())
                    } else {
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<List<ActivoModel>>, t: Throwable) {
                    callback(null)
                }
            })
    }
}