package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.Cuenta
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/api/cuentas")
    fun guardarCuenta(@Body cuenta:Cuenta):Call<Cuenta>

    @GET("api/cuentas/{id}")
    fun obtenerCuentaPorId(@Path("id") id: Long): Call<Cuenta>

    @GET("/api/cuentas")
    fun buscarCuentas():Call<List<Cuenta>>
}