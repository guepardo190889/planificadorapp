package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.portafolios.PortafolioGuardarRequestModel
import com.example.planificadorapp.modelos.portafolios.PortafolioModel
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarResponseModel
import com.example.planificadorapp.modelos.portafolios.graficos.DistribucionPortafolioGraficoModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
     * Busca un portafolio por su ID en el servidor y devuelve un PortafolioBuscarResponseModel
     */
    @GET("api/portafolios/{id}")
    fun buscarPortafolioPorId(@Path("id") id: Long): Call<PortafolioBuscarResponseModel>

    /**
     * Busca los portafolios en el servidor y devuelve una lista de PortafolioModel
     */
    @GET("api/portafolios")
    fun buscarPortafolios(): Call<List<PortafolioModel>>

    /**
     * Busca en el servidor los datos necesarios para generar un gráfico de distribución y devuelve un DistribucionPortafolioGraficoModel
     */
    @GET("api/portafolios/{id}/grafico-distribucion")
    fun buscarDatosGraficoDistribucion(@Path("id") id: Long): Call<DistribucionPortafolioGraficoModel>
}