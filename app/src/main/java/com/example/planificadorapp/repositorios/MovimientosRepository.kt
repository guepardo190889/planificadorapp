package com.example.planificadorapp.repositorios

import com.example.planificadorapp.modelos.MovimientoModel
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

/**
 * Repositorio para interactuar con el servicio de movimientos
 */
class MovimientosRepository {
    private val apiService = ApiClient.movimientosService

    /**
     * Busca los movimientos en el servidor y devuelve una lista de MovimientoModel
     */
    fun buscarMovimientos(
        idCuenta: Long,
        fechaInicio: LocalDate?,
        fechaFin: LocalDate?,
        isAlDia: Boolean,
        isMesAnterior: Boolean,
        isDosMesesAtras: Boolean,
        callback: (List<MovimientoModel>?) -> Unit
    ) {
        apiService.buscarMovimientos(
            idCuenta,
            fechaInicio,
            fechaFin,
            isAlDia,
            isMesAnterior,
            isDosMesesAtras
        ).enqueue(object : Callback<List<MovimientoModel>> {
            override fun onResponse(
                call: Call<List<MovimientoModel>>,
                response: Response<List<MovimientoModel>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<MovimientoModel>>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun buscarMovimientosPorCuenta(cuentaSeleccionada: Long): List<MovimientoModel> {
        return emptyList()
    }

    fun buscarMovimientosMesAnterior(cuentaSeleccionada: String): List<MovimientoModel> {
        return emptyList()
    }

    fun buscarMovimientosDosMesesAtras(cuentaSeleccionada: String): List<MovimientoModel> {
        return emptyList()
    }

    fun buscarMovimientosAlDia(cuentaSeleccionada: String): List<MovimientoModel> {
        return emptyList()
    }

    fun buscarMovimientosPorFechas(cuentaSeleccionada: String, fechaInicio: LocalDate, fechaFin: LocalDate): List<MovimientoModel> {
        return emptyList()
    }
}