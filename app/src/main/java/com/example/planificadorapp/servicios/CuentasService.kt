package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.CuentaModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfaz que define los métodos para interactuar con el servicio de cuentas
 */
interface CuentasService {

    @POST("/api/cuentas")
    fun guardarCuenta(@Body cuenta: CuentaModel): Call<CuentaModel>

    @GET("api/cuentas/{id}")
    fun obtenerCuentaPorId(@Path("id") id: Long): Call<CuentaModel>

    @GET("/api/cuentas")
    fun buscarCuentas(): Call<List<CuentaModel>>

    @PUT("/api/cuentas/{id}")
    fun actualizarCuenta(@Path("id") id: Long, @Body cuenta: CuentaModel): Call<CuentaModel>
}