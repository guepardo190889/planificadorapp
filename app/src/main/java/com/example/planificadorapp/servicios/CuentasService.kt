package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.cuentas.GuardarCuentaRequestModel
import com.example.planificadorapp.modelos.cuentas.ActualizarCuentaRequestModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define los métodos para interactuar con el servicio de cuentas
 */
interface CuentasService {

    /**
     * Guarda una cuenta en el servidor y devuelve la cuenta guardada
     */
    @POST("/api/cuentas")
    fun guardarCuenta(@Body cuenta: GuardarCuentaRequestModel): Call<CuentaModel>

    /**
     * Actualiza una cuenta en el servidor y devuelve la cuenta actualizada
     */
    @PUT("/api/cuentas/{id}")
    fun actualizarCuenta(
        @Path("id") id: Long,
        @Body cuenta: ActualizarCuentaRequestModel
    ): Call<CuentaModel>

    /**
     * Obtiene una cuenta por su ID en el servidor y devuelve la cuenta encontrada
     */
    @GET("api/cuentas/{id}")
    fun obtenerCuentaPorId(@Path("id") id: Long): Call<CuentaModel>

    /**
     * Busca cuentas en el servidor y devuelve una lista de CuentaModel
     */
    @GET("/api/cuentas")
    fun buscarCuentas(
        @Query("excluirCuentasAsociadas") excluirCuentasAsociadas: Boolean,
        @Query("incluirSoloCuentasNoAgrupadorasSinAgrupar") incluirSoloCuentasNoAgrupadorasSinAgrupar: Boolean
    ): Call<List<CuentaModel>>

    /**
     * Busca subcuentas de una cuenta en el servidor y devuelve una lista de CuentaModel
     */
    @GET("/api/cuentas/{id}/subcuentas")
    fun buscarSubcuentas(@Path("id") id: Long): Call<List<CuentaModel>>

}