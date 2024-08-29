package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.ActivoModel
import com.example.planificadorapp.modelos.TransaccionActivoRequestModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define los métodos para interactuar con el servicio de activos
 */
interface ActivosService {

    /**
     * Guarda un activo en el servidor y devuelve el activo guardado
     */
    @POST("/api/activos")
    fun guardarActivo(@Body activo: TransaccionActivoRequestModel): Call<ActivoModel>

    /**
     * Actualiza un activo en el servidor y devuelve el activo actualizado
     */
    @PUT("api/activos/{id}")
    fun actualizarActivo(
        @Path("id") id: Long,
        @Body activo: TransaccionActivoRequestModel
    ): Call<ActivoModel>

    /**
     * Busca un activo por su ID en el servidor y devuelve el activo encontrado
     */
    @GET("/api/activos/{id}")
    fun buscarActivoPorId(@Path("id") id: Long): Call<ActivoModel>

    /**
     * Busca los portafolios en el servidor y devuelve una lista de ActivoModel
     */
    @GET("api/activos")
    fun buscarActivos(@Query("incluirSoloActivosPadre") incluirSoloActivosPadre: Boolean): Call<List<ActivoModel>>

}