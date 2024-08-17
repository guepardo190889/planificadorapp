package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.PortafolioModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interfaz que define los métodos para interactuar con el servicio de portafolios
 */
interface PortafoliosService {

    /**
     * Busca los portafolios en el servidor y devuelve una lista de PortafolioModel
     */
    @GET("api/portafolios")
    fun buscarPortafolios(): Call<List<PortafolioModel>>


}