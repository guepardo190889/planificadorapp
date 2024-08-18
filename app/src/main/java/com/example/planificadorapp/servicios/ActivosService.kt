package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.ActivoModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interfaz que define los métodos para interactuar con el servicio de activos
 */
interface ActivosService{

    /**
     * Busca los portafolios en el servidor y devuelve una lista de ActivoModel
     */
    @GET("api/activos")
    fun buscarActivos(): Call<List<ActivoModel>>
}