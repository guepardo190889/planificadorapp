package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.ActivoGuardarRequestModel
import com.example.planificadorapp.modelos.ActivoModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interfaz que define los métodos para interactuar con el servicio de activos
 */
interface ActivosService {

    @POST("/api/activos")
    fun guardarActivo(@Body activo: ActivoGuardarRequestModel): Call<ActivoModel>

    /**
     * Busca los portafolios en el servidor y devuelve una lista de ActivoModel
     */
    @GET("api/activos")
    fun buscarActivos(@Query("incluirSoloActivosPadre") incluirSoloActivosPadre: Boolean): Call<List<ActivoModel>>
}