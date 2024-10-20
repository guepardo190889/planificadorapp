package com.blackdeath.planificadorapp.repositorios

import android.util.Log
import com.blackdeath.planificadorapp.modelos.movimientos.MovimientoModel
import com.blackdeath.planificadorapp.modelos.movimientos.TransaccionMovimientoRequestModel
import com.blackdeath.planificadorapp.servicios.ApiClient
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
     * Guarda un movimiento en el servidor y devuelve el movimiento guardado
     */
    fun guardarMovimiento(
        movimiento: TransaccionMovimientoRequestModel,
        onResult: (MovimientoModel?) -> Unit
    ) {
        Log.i("MovimientosRepository", "Guardando movimiento: $movimiento")
        val call = apiService.guardarMovimiento(movimiento)
        call.enqueue(object : Callback<MovimientoModel> {
            override fun onResponse(
                call: Call<MovimientoModel>,
                response: Response<MovimientoModel>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body()!!)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<MovimientoModel>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun actualizarMovimiento(
        id: Long,
        movimiento: TransaccionMovimientoRequestModel,
        callback: (MovimientoModel?) -> Unit
    ) {
        val call = apiService.actualizarMovimiento(id, movimiento)
        call.enqueue(object : Callback<MovimientoModel> {
            override fun onResponse(
                call: Call<MovimientoModel>,
                response: Response<MovimientoModel>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                }
            }

            override fun onFailure(call: Call<MovimientoModel>, t: Throwable) {
                callback(null)
            }
        })
    }

    /**
     * Obtiene un movimiento por su ID en el servidor y devuelve el movimiento encontrado
     */
    fun buscarMovimientoPorId(
        id: Long,
        callback: (MovimientoModel?) -> Unit
    ) {
        val call = apiService.buscarMovimientoPorId(id)
        call.enqueue(object : Callback<MovimientoModel> {
            override fun onResponse(
                call: Call<MovimientoModel>,
                response: Response<MovimientoModel>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<MovimientoModel>, t: Throwable) {
                callback(null)
            }
        })
    }

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
}