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
import com.example.planificadorapp.pantallas.portafolios.PortafolioAsignacionCuentasConActivos
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
                    Log.i("EditarPortafolio", "Activos encontrados: $activosEncontrados")
                    activos = activosEncontrados ?: emptyList()

                    if (activos.isNotEmpty()) {
                        //Buscar cuentas
                        cuentasRepository.buscarCuentas(
                            excluirCuentasAsociadas = false,
                            incluirSoloCuentasNoAgrupadorasSinAgrupar = false
                        ) { cuentasEncontradas ->
                            cuentas = cuentasEncontradas ?: emptyList()
                            Log.i("EditarPortafolio", "Cuentas encontradas: $cuentasEncontradas")

                            if (cuentas.isNotEmpty()) {
                                //Armar composiciones con activos y cuentas
                                composiciones =
                                    portafolioEncontrado.composiciones.map { composicion ->
                                        val activo = activos.find { it.id == composicion.idActivo }
                                        val cuentasPortafolio =
                                            cuentas.filter { cuenta -> cuenta.id in composicion.cuentas.map { it.id } }

                                        GuardarComposicionModel(
                                            activo = activo!!,
                                            porcentaje = composicion.porcentaje.toFloat(),
                                            cuentas = cuentasPortafolio
                                        )
                                    }
                            }

                            isDatosCargados = true

//                            //TODO Implementar método que me traiga toda las cuentas NO AGRUPADORAS (o a menos que esta lista la manipule manualmente para solo poder seleccionar lo permitido)
//                            //Buscar todas las cuentas no asociadas a ninguna composición (TODO Revisar cómo se deben mostrar estas cuentas para que el usuario las elija ahora que hay cuentas agrupadoras)
//                            cuentasRepository.buscarCuentas(
//                                excluirCuentasAsociadas = true,
//                                incluirSoloCuentasNoAgrupadorasSinAgrupar = false
//                            ) { cuentasNoAsociadasEncontradas ->
//                                cuentasDisponiblesParaAsociar =
//                                    cuentasNoAsociadasEncontradas ?: emptyList()
//                                Log.i(
//                                    "ActualizarPortafolio",
//                                    "Cuentas no asociadas encontradas: $cuentasDisponiblesParaAsociar"
//                                )
//
//                                isDatosCargados = true
//                            }
                        }
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
            Log.i("EditarPortafolio", "Paso actual: $pasoActual")

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
                        LaunchedEffect(Unit) {
                            activosRepository.buscarActivos(false) { result ->
                                activos = result ?: emptyList()
                            }
                        }

                        PortafolioDistribucionActivos(modifier = modifier,
                            activos = activos,
                            composiciones = composiciones,
                            onAgregarComposicion = { nuevaComposicion ->
                                Log.i(
                                    "ActualizarPortafolio",
                                    "Agregando nueva composición: $nuevaComposicion"
                                )
                                Log.i(
                                    "ActualizarPortafolio",
                                    "Composiciones actuales (agregar): $composiciones"
                                )
                                composiciones = composiciones + nuevaComposicion
                                Log.i(
                                    "ActualizarPortafolio",
                                    "Composiciones actualizadas (agregar): $composiciones"
                                )
                            },
                            onEliminarComposicion = { composicionAEliminar ->
                                Log.i(
                                    "ActualizarPortafolio",
                                    "Eliminando composición: $composicionAEliminar"
                                )
                                Log.i(
                                    "ActualizarPortafolio",
                                    "Composiciones actuales (eliminar): $composiciones"
                                )
                                composiciones = composiciones.filter { it != composicionAEliminar }
                                Log.i(
                                    "ActualizarPortafolio",
                                    "Composiciones actualizadas (eliminar): $composiciones"
                                )
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
                        LaunchedEffect(Unit) {
                            cuentasRepository.buscarCuentas(
                                excluirCuentasAsociadas = true,
                                incluirSoloCuentasNoAgrupadorasSinAgrupar = false
                            ) { cuentasEncontradas ->
                                cuentas = cuentasEncontradas ?: emptyList()

                                cuentasDisponiblesParaAsociar = cuentas.filter { cuenta ->
                                    composiciones.none { composicion ->
                                        composicion.cuentas.any { it.id == cuenta.id }
                                    }
                                }
                            }
                        }

                        PortafolioAsignacionCuentasConActivos(
                            modifier = modifier,
                            composiciones = composiciones,
                            cuentas = cuentas,
                            onAsignarCuenta = { composicion, cuentaSeleccionada ->
                                composiciones = composiciones.map {
                                    if (it == composicion) {
                                        it.copy(cuentas = it.cuentas + cuentaSeleccionada)
                                    } else it
                                }

                                cuentasDisponiblesParaAsociar = cuentas.filter { cuenta ->
                                    composiciones.none { composicion ->
                                        composicion.cuentas.any { it.id == cuenta.id }
                                    }
                                }
                            },
                            onDesasignarCuenta = { composicion, cuentaSeleccionada ->
                                composiciones = composiciones.map {
                                    if (it == composicion) {
                                        it.copy(cuentas = it.cuentas - cuentaSeleccionada)
                                    } else it
                                }

                                cuentasDisponiblesParaAsociar = cuentas.filter { cuenta ->
                                    composiciones.none { composicion ->
                                        composicion.cuentas.any { it.id == cuenta.id }
                                    }
                                }
                            },
                            onAtrasClick = {
                                pasoActual = PasoWizard.PASO_DOS
                            },
                            onSiguienteClick = {
                                pasoActual = PasoWizard.PASO_RESUMEN
                            }
                        )
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

                                portafoliosRepository.guardarPortafolio(portafolioPorActualizar) { portafolioActualizado ->
                                    Log.i(
                                        "GuardarPortafolio",
                                        "Portafolio guardado: $portafolioActualizado"
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