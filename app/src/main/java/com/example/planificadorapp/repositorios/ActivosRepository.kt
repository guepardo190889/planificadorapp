package com.example.planificadorapp.repositorios

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
     * Busca los activos en el servidor y devuelve una lista de ActivoModel.
     */
    fun buscarActivos(callback: (List<ActivoModel>?) -> Unit) {
        apiService.buscarActivos().enqueue(object : Callback<List<ActivoModel>> {
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