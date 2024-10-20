package com.blackdeath.planificadorapp.repositorios

import com.blackdeath.planificadorapp.modelos.reportes.GraficoPastelModel
import com.blackdeath.planificadorapp.modelos.reportes.ReporteMenuResponseModel
import com.blackdeath.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositorio para interactuar con el servicio de reportes
 */
class ReportesRepository {
    private val apiService = ApiClient.reportesService

    /**
     * Busca todos los reportes en el servidor y devuelve una lista de reportes encontrados
     */
    fun buscarReportes(onResult: (List<ReporteMenuResponseModel>?) -> Unit) {
        apiService.buscarReportes().enqueue(object : Callback<List<ReporteMenuResponseModel>> {
            override fun onResponse(
                call: Call<List<ReporteMenuResponseModel>>,
                response: Response<List<ReporteMenuResponseModel>>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<ReporteMenuResponseModel>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    /**
     * Genera un reporte de distribución de activos de un portafolio en el servidor y devuelve el reporte generado
     */
    fun generarReporteDistribucionActivosPortafolio(
        idPortafolio: Long, onResult: (GraficoPastelModel?) -> Unit
    ) {
        apiService.generarReporteDistribucionActivosPortafolio(idPortafolio)
            .enqueue(object : Callback<GraficoPastelModel> {
                override fun onResponse(
                    call: Call<GraficoPastelModel>, response: Response<GraficoPastelModel>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<GraficoPastelModel>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    /**
     * Busca un reporte de distribución de saldos de un portafolio en el servidor y devuelve el reporte encontrado
     */
    fun buscarReporteDistribucionSaldosPortafolio(
        idPortafolio: Long, onResult: (GraficoPastelModel?) -> Unit
    ) {
        apiService.buscarReporteDistribucionSaldosPortafolio(idPortafolio)
            .enqueue(object : Callback<GraficoPastelModel> {
                override fun onResponse(
                    call: Call<GraficoPastelModel>, response: Response<GraficoPastelModel>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<GraficoPastelModel>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    /**
     * Busca un reporte de historico de saldos de una cuenta en el servidor y devuelve el reporte encontrado
     */
    fun buscarReporteHistoricoSaldosCuenta(
        idCuenta: Long, anio: Int, onResult: (GraficoPastelModel?) -> Unit
    ) {
        apiService.buscarReporteHistoricoSaldosCuenta(idCuenta, anio)
            .enqueue(object : Callback<GraficoPastelModel> {
                override fun onResponse(
                    call: Call<GraficoPastelModel>, response: Response<GraficoPastelModel>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<GraficoPastelModel>, t: Throwable) {
                    onResult(null)
                }
            })
    }

}