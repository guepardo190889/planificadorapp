package com.example.planificadorapp.repositorios

import android.util.Log
import com.example.planificadorapp.modelos.portafolios.PortafolioGuardarRequestModel
import com.example.planificadorapp.modelos.portafolios.PortafolioModel
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarResponseModel
import com.example.planificadorapp.modelos.portafolios.graficos.DistribucionPortafolioGraficoModel
import com.example.planificadorapp.servicios.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositorio para interactuar con el servicio de portafolios
 */
class PortafoliosRepository {
    private val apiService = ApiClient.portafoliosService

    /**
     * Guarda un portafolio en el servidor y devuelve el portafolio guardado
     */
    fun guardarPortafolio(
        portafolio: PortafolioGuardarRequestModel,
        onResult: (PortafolioModel?) -> Unit
    ) {
        val call = ApiClient.portafoliosService.guardarPortafolio(portafolio)
        call.enqueue(object : Callback<PortafolioModel> {
            override fun onResponse(
                call: Call<PortafolioModel>,
                response: Response<PortafolioModel>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<PortafolioModel>, t: Throwable) {
                Log.e("PortafoliosRepository", "Error al guardar el portafolio", t)
                onResult(null)
            }
        })
    }

    /**
     *  Actualiza un portafolio en el servidor y devuelve el portafolio actualizado
     */
    fun actualizarPortafolio(
        idPortafolio: Long,
        portafolio: PortafolioGuardarRequestModel,
        onResult: (PortafolioModel?) -> Unit
    ) {
        val call = apiService.actualizarPortafolio(idPortafolio, portafolio)
        call.enqueue(object : Callback<PortafolioModel> {
            override fun onResponse(
                call: Call<PortafolioModel>,
                response: Response<PortafolioModel>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }

            }

            override fun onFailure(call: Call<PortafolioModel>, t: Throwable) {
                onResult(null)
            }
        })
    }

    /**
     * Busca un portafolio por su ID en el servidor y devuelve un PortafolioBuscarResponseModel
     */
    fun buscarPortafolioPorId(id: Long, onResult: (PortafolioBuscarResponseModel?) -> Unit) {
        apiService.buscarPortafolioPorId(id)
            .enqueue(object : Callback<PortafolioBuscarResponseModel> {
                override fun onResponse(
                    call: Call<PortafolioBuscarResponseModel>,
                    response: Response<PortafolioBuscarResponseModel>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<PortafolioBuscarResponseModel>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    /**
     * Busca los portafolios en el servidor y devuelve una lista de PortafolioModel
     */
    fun buscarPortafolios(onResult: (List<PortafolioModel>?) -> Unit) {
        apiService.buscarPortafolios().enqueue(object : Callback<List<PortafolioModel>> {
            override fun onResponse(
                call: Call<List<PortafolioModel>>,
                response: Response<List<PortafolioModel>>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<PortafolioModel>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    /**
     * Busca en el servidor los datos necesarios para generar un gráfico de distribución y devuelve un DistribucionPortafolioGraficoModel
     */
    fun buscarDatosGraficoDistribucion(
        idPortafolio: Long,
        onResult: (DistribucionPortafolioGraficoModel?) -> Unit
    ) {
        apiService.buscarDatosGraficoDistribucion(idPortafolio)
            .enqueue(object : Callback<DistribucionPortafolioGraficoModel> {
                override fun onResponse(
                    call: Call<DistribucionPortafolioGraficoModel>,
                    response: Response<DistribucionPortafolioGraficoModel>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(
                    call: Call<DistribucionPortafolioGraficoModel>,
                    t: Throwable
                ) {
                    onResult(null)
                }
            })
    }
}