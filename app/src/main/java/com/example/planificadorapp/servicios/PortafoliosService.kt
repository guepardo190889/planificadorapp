package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.PortafolioGuardarRequestModel
import com.example.planificadorapp.modelos.PortafolioModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interfaz que define los métodos para interactuar con el servicio de portafolios
 */
interface PortafoliosService {

    /**
     * Guarda un portafolio en el servidor y devuelve el portafolio guardado
     */
    @POST("api/portafolios")
    fun guardarPortafolio(@Body portafolio: PortafolioGuardarRequestModel): Call<PortafolioModel>

    /**
     * Busca los portafolios en el servidor y devuelve una lista de PortafolioModel
     */
    @GET("api/portafolios")
    fun buscarPortafolios(): Call<List<PortafolioModel>>


}