package com.blackdeath.planificadorapp.servicios

import com.blackdeath.planificadorapp.modelos.portafolios.PortafolioGuardarRequestModel
import com.blackdeath.planificadorapp.modelos.portafolios.PortafolioModel
import com.blackdeath.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfaz que define los métodos para interactuar con el servicio de portafolios
 */
interface PortafoliosService {

    /**
     * Guarda un portafolio en el servidor y devuelve el portafolio guardado
     */
    @POST("api/portafolios")
    fun guardarPortafolio(@Body portafolio: PortafolioGuardarRequestModel): Call<PortafolioModel>

    /**
     * Actualiza un portafolio en el servidor y devuelve el portafolio actualizado
     */
    @PUT("api/portafolios/{id}")
    fun actualizarPortafolio(
        @Path("id") id: Long,
        @Body portafolio: PortafolioGuardarRequestModel
    ): Call<PortafolioModel>

    /**
     * Busca un portafolio por su ID en el servidor y devuelve un PortafolioBuscarResponseModel
     */
    @GET("api/portafolios/{id}")
    fun buscarPortafolioPorId(@Path("id") id: Long): Call<PortafolioBuscarResponseModel>

    /**
     * Busca los portafolios en el servidor y devuelve una lista de PortafolioModel
     */
    @GET("api/portafolios")
    fun buscarPortafolios(): Call<List<PortafolioModel>>
}