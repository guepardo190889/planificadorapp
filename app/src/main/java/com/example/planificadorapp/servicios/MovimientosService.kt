package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.MovimientoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

/**
 * Interfaz que define los métodos para interactuar con el servicio de movimientos
 */
interface MovimientosService {

    /**
     * Busca los movimientos en el servidor y devuelve una lista de MovimientoModel
     */
    @GET("api/movimientos")
    fun buscarMovimientos(
        @Query("idCuenta") idCuenta: Long,
        @Query("fechaInicio") fechaInicio: LocalDate?,
        @Query("fechaFin") fechaFin: LocalDate?,
        @Query("isAlDia") isAlDia: Boolean,
        @Query("isMesAnterior") isMesAnterior: Boolean,
        @Query("isDosMesesAtras") isDosMesesAtras: Boolean
    ): Call<List<MovimientoModel>>
}