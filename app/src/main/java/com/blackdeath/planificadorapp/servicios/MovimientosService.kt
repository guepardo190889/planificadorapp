package com.blackdeath.planificadorapp.servicios

import com.blackdeath.planificadorapp.modelos.movimientos.MovimientoModel
import com.blackdeath.planificadorapp.modelos.movimientos.TransaccionMovimientoRequestModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

/**
 * Interfaz que define los métodos para interactuar con el servicio de movimientos
 */
interface MovimientosService {

    /**
     * Guarda un movimiento en el servidor y devuelve el movimiento guardado
     */
    @POST("api/movimientos")
    fun guardarMovimiento(@Body movimiento: TransaccionMovimientoRequestModel): Call<MovimientoModel>

    /**
     * Actualiza un movimiento en el servidor y devuelve el movimiento actualizado
     */
    @PUT("api/movimientos/{id}")
    fun actualizarMovimiento(
        @Path("id") id: Long,
        @Body movimiento: TransaccionMovimientoRequestModel
    ): Call<MovimientoModel>

    /**
     * Obtiene un movimiento por su ID en el servidor y devuelve el movimiento encontrado
     */
    @GET("api/movimientos/{id}")
    fun buscarMovimientoPorId(@Path("id") id: Long): Call<MovimientoModel>

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