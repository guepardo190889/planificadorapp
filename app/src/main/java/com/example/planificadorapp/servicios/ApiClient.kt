package com.example.planificadorapp.servicios

import com.example.planificadorapp.tipos.LocalDateTimeTypeAdapter
import com.example.planificadorapp.tipos.LocalDateTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Objeto que representa el cliente de la API
 */
object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080"

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        .create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val cuentaService: CuentasService = retrofit.create(CuentasService::class.java)
    val portafoliosService: PortafoliosService = retrofit.create(PortafoliosService::class.java)
    val activosService: ActivosService = retrofit.create(ActivosService::class.java)
    val movimientosService: MovimientosService = retrofit.create(MovimientosService::class.java)
}
