package com.example.planificadorapp.repositorios

import com.example.planificadorapp.modelos.PortafolioModel
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositorio para interactuar con el servicio de portafolios
 */
class PortafoliosRepository {
    private val apiService = ApiClient.portafoliosService

    /**
     * Busca los portafolios en el servidor y devuelve una lista de PortafolioModel.
     */
    fun buscarPortafolios(callback: (List<PortafolioModel>?) -> Unit) {
        apiService.buscarPortafolios().enqueue(object : Callback<List<PortafolioModel>> {
            override fun onResponse(
                call: Call<List<PortafolioModel>>,
                response: Response<List<PortafolioModel>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<PortafolioModel>>, t: Throwable) {
                callback(null)
            }
        })
    }

}