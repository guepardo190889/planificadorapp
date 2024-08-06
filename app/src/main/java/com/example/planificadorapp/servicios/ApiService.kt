package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.Cuenta
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/cuentas")
    fun guardarCuenta(@Body cuenta:Cuenta):Call<Cuenta>
}