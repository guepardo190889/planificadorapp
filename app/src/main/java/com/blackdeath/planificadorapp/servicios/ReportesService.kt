package com.blackdeath.planificadorapp.servicios

import com.blackdeath.planificadorapp.modelos.reportes.GraficoPastelModel
import com.blackdeath.planificadorapp.modelos.reportes.ReporteMenuResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
    fun generarReporteDistribucionActivosPortafolio(@Path("id") id: Long):Call<GraficoPastelModel>

    /**
     * Busca el reporte de distribución de saldos de un portafolio en el servidor y devuelve un GraficoPastelModel
     */
    @GET("/api/v1/portafolios/{id}/reportes/distribucion-saldos")
    fun buscarReporteDistribucionSaldosPortafolio(@Path("id") id: Long):Call<GraficoPastelModel>

    /**
     * Busca el reporte de historico de saldos de una cuenta en el servidor y devuelve un GraficoPastelModel
     */
    @GET("/api/v1/cuentas/{id}/reportes/historico-saldos")
    fun buscarReporteHistoricoSaldosCuenta( @Path("id") id: Long, @Query("anio") anio: Int):Call<GraficoPastelModel>
}