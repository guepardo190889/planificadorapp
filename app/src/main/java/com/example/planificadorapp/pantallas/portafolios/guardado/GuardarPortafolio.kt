package com.example.planificadorapp.pantallas.portafolios.guardado

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.planificadorapp.composables.snackbar.SnackBarBase
import com.example.planificadorapp.composables.snackbar.SnackBarManager
import com.example.planificadorapp.composables.snackbar.SnackBarTipo
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.composiciones.ComposicionGuardarRequestModel
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.modelos.portafolios.PortafolioGuardarRequestModel
import com.example.planificadorapp.navegacion.Ruta
import com.example.planificadorapp.pantallas.portafolios.PasoWizard
import com.example.planificadorapp.pantallas.portafolios.PortafolioAsociacionCuentasConActivos
import com.example.planificadorapp.pantallas.portafolios.PortafolioDatosGenerales
import com.example.planificadorapp.pantallas.portafolios.PortafolioDistribucionActivos
import com.example.planificadorapp.pantallas.portafolios.ResumenPortafolio
import com.example.planificadorapp.repositorios.ActivosRepository
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.PortafoliosRepository

/**
 * Composable que representa la pantalla de guardado de un portafolio
 */
@Composable
fun GuardarPortafolio(modifier: Modifier = Modifier, navController: NavController) {
    val activosRepository = ActivosRepository()
    val cuentasRepository = CuentasRepository()
    val portafoliosRepository = PortafoliosRepository()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarManager = remember { SnackBarManager(coroutineScope, snackbarHostState) }

    var pasoActual: PasoWizard by remember { mutableStateOf(PasoWizard.PASO_UNO) }

    //Paso Uno
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    //Paso Dos
    var activos by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var composiciones by remember { mutableStateOf<List<GuardarComposicionModel>>(emptyList()) }
    var totalPorcentaje by remember { mutableIntStateOf(0) }

    //Paso Tres
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var cuentasDisponiblesParaAsociar by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }

    var isGuardando by remember { mutableStateOf(false) }

    /**
     * Crea un modelo de datos para guardar un portafolio
     */
    fun crearModeloGuardado(): PortafolioGuardarRequestModel {
        val composicionesPorGuardar = mutableListOf<ComposicionGuardarRequestModel>()

        for (composicion in composiciones) {
            val cuentasPorGuardar = mutableListOf<Long>()

            for (cuenta in composicion.cuentas) {
                cuentasPorGuardar.add(cuenta.id)
            }

            composicionesPorGuardar.add(
                ComposicionGuardarRequestModel(
                    composicion.activo.id, composicion.porcentaje.toInt(), cuentasPorGuardar
                )
            )
        }

        val portafolio = PortafolioGuardarRequestModel(nombre, descripcion, composicionesPorGuardar)

        return portafolio
    }

    Scaffold(modifier = modifier.fillMaxSize(), snackbarHost = {
        SnackBarBase(
            snackbarHostState = snackbarHostState, snackBarManager = snackBarManager
        )
    }, content = { paddingValues ->
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Log.i("GuardarPortafolio", "Paso actual: $pasoActual")

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
                            Log.i("GuardarPortafolioPasoDos", "Cargando activos...")
                            activosRepository.buscarActivos(false) { result ->
                                activos = result ?: emptyList()
                            }
                        }

                        PortafolioDistribucionActivos(modifier = modifier,
                            activos = activos,
                            composiciones = composiciones,
                            onAgregarComposicion = { nuevaComposicion ->
                                composiciones = composiciones + nuevaComposicion
                            },
                            onEliminarComposicion = { composicionAEliminar ->
                                composiciones = composiciones.filter { it != composicionAEliminar }
                            },
                            onPorcentajeCambiado = { composicion, nuevoPorcentaje ->
                                composiciones = composiciones.map {
                                    if (it.activo == composicion.activo) {
                                        it.copy(porcentaje = nuevoPorcentaje.toFloat())
                                    } else it
                                }
                                totalPorcentaje = composiciones.sumOf { it.porcentaje.toInt() }
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
                            Log.i("GuardarPortafolio", "Cargando cuentas...")
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

                        PortafolioAsociacionCuentasConActivos(modifier = modifier,
                            composiciones = composiciones,
                            cuentas = cuentasDisponiblesParaAsociar,
                            onAsociarCuenta = { composicion, cuentaSeleccionada ->
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
                            onDesasociarCuenta = { composicion, cuentaSeleccionada ->
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
                            })
                    }

                    PasoWizard.PASO_RESUMEN -> {
                        ResumenPortafolio(modifier,
                            nombre,
                            descripcion,
                            composiciones,
                            onAtrasClick = {
                                pasoActual = PasoWizard.PASO_TRES
                            },
                            onTransaccionClick = {
                                isGuardando = true

                                val portafolioPorGuardar = crearModeloGuardado()

                                Log.i(
                                    "GuardarPortafolio",
                                    "Guardando portafolio... $portafolioPorGuardar"
                                )

                                portafoliosRepository.guardarPortafolio(portafolioPorGuardar) { portafolioGuardado ->
                                    Log.i(
                                        "GuardarPortafolio",
                                        "Portafolio guardado: $portafolioGuardado"
                                    )

                                    if (portafolioGuardado != null) {
                                        snackBarManager.mostrar(
                                            "Portafolio guardado exitosamente", SnackBarTipo.SUCCESS
                                        ) {
                                            isGuardando = false
                                            navController.navigate(Ruta.PORTAFOLIOS.ruta)
                                        }
                                    } else {
                                        snackBarManager.mostrar(
                                            "Error al guardar el portafolio", SnackBarTipo.ERROR
                                        ) {
                                            isGuardando = false
                                        }
                                    }
                                }
                            })
                    }
                }
            }

            if (isGuardando) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                        .clickable(enabled = false) {}, contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    })
}
