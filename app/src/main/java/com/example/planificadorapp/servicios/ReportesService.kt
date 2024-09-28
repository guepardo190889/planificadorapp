package com.example.planificadorapp.servicios

import com.example.planificadorapp.modelos.reportes.GraficoPastelModel
import com.example.planificadorapp.modelos.reportes.ReporteMenuResponseModel
import com.example.planificadorapp.modelos.reportes.ReporteModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz que define los métodos para interactuar con el servicio de reportes
 */
interface ReportesService{

    /**
     * Busca el menú de reportes en el servidor y devuelve una lista de ReporteMenuResponseModel
     */
    @GET("/api/reportes/menu")
    fun buscarReportes(): Call<List<ReporteMenuResponseModel>>

    /**
     * Busca el reporte de distribución de activos de un portafolio en el servidor y devuelve un GraficoPastelModel
     */
    @GET("/api/v1/portafolios/{id}/reportes/distribucion-activos")
    fun generarReporteDistribucionActivos( @Path("id") id: Long):Call<GraficoPastelModel>
}