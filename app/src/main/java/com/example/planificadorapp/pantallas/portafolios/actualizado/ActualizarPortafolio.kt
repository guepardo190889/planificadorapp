package com.example.planificadorapp.pantallas.portafolios.actualizado

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.composiciones.ComposicionGuardarRequestModel
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.portafolios.PortafolioGuardarRequestModel
import com.example.planificadorapp.modelos.portafolios.busqueda.PortafolioBuscarResponseModel
import com.example.planificadorapp.pantallas.portafolios.PasoWizard
import com.example.planificadorapp.pantallas.portafolios.PortafolioAsociacionCuentasConActivos
import com.example.planificadorapp.pantallas.portafolios.PortafolioDatosGenerales
import com.example.planificadorapp.pantallas.portafolios.PortafolioDistribucionActivos
import com.example.planificadorapp.pantallas.portafolios.ResumenPortafolio
import com.example.planificadorapp.repositorios.ActivosRepository
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.PortafoliosRepository
import kotlinx.coroutines.launch

/**
 * Composable que representa la pantalla de actualización de un portafolio
 */
@Composable
fun ActualizarPortafolio(
    modifier: Modifier = Modifier, navController: NavController, idPortafolio: Long
) {
    val activosRepository = ActivosRepository()
    val cuentasRepository = CuentasRepository()
    val portafoliosRepository = PortafoliosRepository()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

    var pasoActual: PasoWizard by remember { mutableStateOf(PasoWizard.PASO_UNO) }

    // Variables para cargar el portafolio actual
    var portafolio by remember { mutableStateOf<PortafolioBuscarResponseModel?>(null) }

    //Paso Uno
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    //Paso Dos
    var activos by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var composiciones by remember { mutableStateOf<List<GuardarComposicionModel>>(emptyList()) }

    //Paso Tres
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var cuentasDisponiblesTemporalmente by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var cuentasDisponiblesParaAsociar by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }

    var isDatosCargados by remember { mutableStateOf(false) }

    /**
     * Crea el modelo de datos para actualizar un portafolio
     */
    fun crearModeloActualizado(): PortafolioGuardarRequestModel {
        val composicionesPorGuardar = composiciones.map { composicion ->
            val cuentasPorGuardar = composicion.cuentas.map { it.id }
            ComposicionGuardarRequestModel(
                idActivo = composicion.activo.id,
                porcentaje = composicion.porcentaje.toInt(),
                idCuentas = cuentasPorGuardar
            )
        }
        return PortafolioGuardarRequestModel(nombre, descripcion, composicionesPorGuardar)
    }

    LaunchedEffect(idPortafolio) {
        //Buscar portafolio
        portafoliosRepository.buscarPortafolioPorId(idPortafolio) { portafolioEncontrado ->
            Log.i("EditarPortafolio", "Portafolio encontrado: $portafolioEncontrado")

            if (portafolioEncontrado != null) {
                portafolio = portafolioEncontrado
                nombre = portafolioEncontrado.nombre ?: ""
                descripcion = portafolioEncontrado.descripcion ?: ""

                //Buscar todos los activos
                activosRepository.buscarActivos(incluirSoloActivosPadre = false) { activosEncontrados ->
                    activos = activosEncontrados ?: emptyList()

                    Log.i("ActualizarPortafolio", "Activos encontrados: ${activos.size}")

                    if (activos.isNotEmpty()) {
                        composiciones = portafolioEncontrado.composiciones.map { composicion ->
                            val activo = ActivoModel(composicion.idActivo, composicion.nombreActivo)
                            val cuentasComposicion = mutableListOf<CuentaModel>()

                            composicion.cuentas.forEach { cuenta ->
                                cuentasComposicion.add(
                                    CuentaModel(
                                        id = cuenta.id, nombre = cuenta.nombre, saldo = cuenta.saldo
                                    )
                                )
                            }

                            GuardarComposicionModel(
                                activo = activo,
                                porcentaje = composicion.porcentaje.toFloat(),
                                cuentas = cuentasComposicion
                            )
                        }

                        isDatosCargados = true
                    }
                }
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), snackbarHost = {
        SnackbarHost(snackbarHostState) {
            SnackBarConColor(
                snackbarHostState = snackbarHostState, tipo = snackbarType
            )
        }
    }, content = { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Log.i("ActualizarPortafolio", "Paso actual: $pasoActual")

            if (isDatosCargados) {
                when (pasoActual) {
                    PasoWizard.PASO_UNO -> PortafolioDatosGenerales(modifier,
                        nombre,
                        descripcion,
                        onNombreChange = { nombre = it },
                        onDescripcionChange = { descripcion = it },
                        onSiguienteClick = {
                            pasoActual = PasoWizard.PASO_DOS
                        })

                    PasoWizard.PASO_DOS -> {
                        LaunchedEffect(pasoActual) {
                            if (activos.isEmpty()) {
                                activosRepository.buscarActivos(false) { result ->
                                    activos = result ?: emptyList()

                                    Log.i(
                                        "ActualizarPortafolio",
                                        "Activos encontrados (PASO_DOS): ${activos.size}"
                                    )
                                }
                            }
                        }

                        PortafolioDistribucionActivos(modifier = modifier,
                            activos = activos,
                            composiciones = composiciones,
                            onAgregarComposicion = { nuevaComposicion ->
                                composiciones = composiciones + nuevaComposicion
                            },
                            onEliminarComposicion = { composicionAEliminar ->
                                composiciones = composiciones - composicionAEliminar
                            },
                            onPorcentajeCambiado = { composicion, nuevoPorcentaje ->
                                composiciones = composiciones.map {
                                    if (it == composicion) {
                                        it.copy(porcentaje = nuevoPorcentaje.toFloat())
                                    } else it
                                }
                            },
                            onAtrasClick = {
                                pasoActual = PasoWizard.PASO_UNO
                            },
                            onSiguienteClick = {
                                pasoActual = PasoWizard.PASO_TRES
                            })
                    }

                    PasoWizard.PASO_TRES -> {
                        LaunchedEffect(pasoActual) {
                            if (cuentas.isEmpty()) {
                                cuentasRepository.buscarCuentas(
                                    excluirCuentasAsociadas = true,
                                    incluirSoloCuentasNoAgrupadorasSinAgrupar = false
                                ) { cuentasEncontradas ->
                                    cuentas = cuentasEncontradas ?: emptyList()

                                    Log.i(
                                        "ActualizarPortafolio",
                                        "Cuentas encontradas (PASO_TRES): ${cuentas.size}"
                                    )

                                    cuentasDisponiblesParaAsociar = cuentas.filter { cuenta ->
                                        composiciones.none { composicion ->
                                            composicion.cuentas.any { it.id == cuenta.id }
                                        }
                                    }
                                }
                            }
                        }

                        PortafolioAsociacionCuentasConActivos(modifier = modifier,
                            composiciones = composiciones,
                            cuentas = cuentasDisponiblesParaAsociar,
                            onAsociarCuenta = { composicion, cuentaSeleccionada ->
                                composiciones = composiciones.map {
                                    if (it == composicion) {
                                        it.copy(cuentas = it.cuentas + cuentaSeleccionada)
                                    } else it
                                }

                                cuentasDisponiblesTemporalmente =
                                    cuentasDisponiblesTemporalmente - cuentaSeleccionada

                                cuentasDisponiblesParaAsociar = cuentas.filter { cuenta ->
                                    composiciones.none { composicion ->
                                        composicion.cuentas.any { it.id == cuenta.id }
                                    }
                                }.toMutableList().apply {
                                    addAll(cuentasDisponiblesTemporalmente)
                                }.toList()
                            },
                            onDesasociarCuenta = { composicion, cuentaSeleccionada ->
                                composiciones = composiciones.map {
                                    if (it == composicion) {
                                        it.copy(cuentas = it.cuentas - cuentaSeleccionada)
                                    } else it
                                }

                                cuentasDisponiblesTemporalmente =
                                    cuentasDisponiblesTemporalmente + cuentaSeleccionada

                                cuentasDisponiblesParaAsociar = cuentas.filter { cuenta ->
                                    composiciones.none { composicion ->
                                        composicion.cuentas.any { it.id == cuenta.id }
                                    }
                                }.toMutableList().apply {
                                    addAll(cuentasDisponiblesTemporalmente)
                                }.toList()
                            },
                            onAtrasClick = {
                                pasoActual = PasoWizard.PASO_DOS
                            },
                            onSiguienteClick = {
                                pasoActual = PasoWizard.PASO_RESUMEN
                            })
                    }

                    PasoWizard.PASO_RESUMEN -> {
                        ResumenPortafolio(modifier,
                            nombre,
                            descripcion,
                            composiciones,
                            isTransaccionGuardar = false,
                            onAtrasClick = {
                                pasoActual = PasoWizard.PASO_TRES
                            },
                            onTransaccionClick = {
                                val portafolioPorActualizar = crearModeloActualizado()

                                Log.i(
                                    "ActualizarPortafolio",
                                    "Actualizando portafolio... $portafolioPorActualizar"
                                )

                                portafoliosRepository.actualizarPortafolio(
                                    idPortafolio = idPortafolio,
                                    portafolio = portafolioPorActualizar
                                ) { portafolioActualizado ->
                                    Log.i(
                                        "ActualizarPortafolio",
                                        "Portafolio actualizado: $portafolioActualizado"
                                    )

                                    if (portafolioActualizado != null) {
                                        snackbarMessage = "Portafolio actualizado exitosamente"
                                        snackbarType = "success"
                                    } else {
                                        snackbarMessage = "Error al actualizar el portafolio"
                                        snackbarType = "error"
                                    }

                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(snackbarMessage)

                                        if (portafolioActualizado != null) {
                                            navController.navigate("portafolios")
                                        }
                                    }
                                }
                            })
                    }
                }
            }
        }
    })
}