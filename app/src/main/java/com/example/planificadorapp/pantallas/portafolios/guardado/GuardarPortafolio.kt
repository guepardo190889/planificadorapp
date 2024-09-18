package com.example.planificadorapp.pantallas.portafolios.guardado

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.pantallas.portafolios.PortafolioDatosGenerales
import com.example.planificadorapp.repositorios.ActivosRepository
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.PortafoliosRepository
import kotlinx.coroutines.launch

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
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SnackBarConColor(
                    snackbarHostState = snackbarHostState,
                    tipo = snackbarType
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Log.i("GuardarPortafolio", "Paso actual: $pasoActual")

                when (pasoActual) {
                    PasoWizard.PASO_UNO ->
                        PortafolioDatosGenerales(
                            modifier,
                            nombre,
                            descripcion,
                            onNombreChange = { nombre = it },
                            onDescripcionChange = { descripcion = it },
                            onSiguienteClick = {
                                pasoActual = PasoWizard.PASO_DOS
                            }
                        )

                    PasoWizard.PASO_DOS -> {
                        LaunchedEffect(Unit) {
                            Log.i("GuardarPortafolioPasoDos", "Cargando activos...")
                            activosRepository.buscarActivos(false) { result ->
                                activos = result ?: emptyList()
                            }
                        }

                        GuardarPortafolioPasoDos(
                            modifier,
                            activos,
                            composiciones,
                            totalPorcentaje,
                            onAtrasClick = { activosSeleccionadosPortafolio, totalPorcentajePortafolio ->
                                composiciones = activosSeleccionadosPortafolio
                                totalPorcentaje = totalPorcentajePortafolio
                                pasoActual = PasoWizard.PASO_UNO
                            },
                            onSiguienteClick = { activosSeleccionadosPortafolio, totalPorcentajePortafolio ->
                                composiciones = activosSeleccionadosPortafolio
                                totalPorcentaje = totalPorcentajePortafolio
                                pasoActual = PasoWizard.PASO_TRES
                            }
                        )
                    }

                    PasoWizard.PASO_TRES -> {
                        LaunchedEffect(Unit) {
                            Log.i("GuardarPortafolioPasoDos", "Cargando cuentas...")
                            cuentasRepository.buscarCuentas(
                                excluirCuentasAsociadas = true,
                                incluirSoloCuentasNoAgrupadorasSinAgrupar = false
                            ) { result ->
                                cuentas = result ?: emptyList()
                            }
                        }

                        GuardarPortafolioPasoTres(
                            modifier,
                            composiciones,
                            cuentas,
                            onAtrasClick = { composicionesPasoTres ->
                                composiciones = composicionesPasoTres
                                pasoActual = PasoWizard.PASO_DOS
                            },
                            onSiguienteClick = { composicionesPasoTres ->
                                composiciones = composicionesPasoTres
                                pasoActual = PasoWizard.PASO_RESUMEN
                            }
                        )
                    }

                    PasoWizard.PASO_RESUMEN -> {
                        GuardarPortafolioResumen(
                            modifier,
                            nombre,
                            descripcion,
                            composiciones,
                            onAtrasClick = {
                                pasoActual = PasoWizard.PASO_TRES
                            },
                            onGuardarClick = { portafolioPorGuardar ->
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
                                        snackbarMessage = "Portafolio guardado exitosamente"
                                        snackbarType = "success"
                                    } else {
                                        snackbarMessage = "Error al guardar el portafolio"
                                        snackbarType = "error"
                                    }

                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(snackbarMessage)

                                        if (portafolioGuardado != null) {
                                            navController.navigate("portafolios")
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}
