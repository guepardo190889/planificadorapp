package com.example.planificadorapp.screens.portafolios.guardado

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
import com.example.planificadorapp.modelos.ActivoModel
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.modelos.GuardarComposicionModel
import com.example.planificadorapp.repositorios.ActivosRepository
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.repositorios.PortafoliosRepository
import kotlinx.coroutines.launch

/**
 * Composable que representa la pantalla de guardado de un portafolio
 */
@Composable
fun GuardarPortafolio(modifier: Modifier = Modifier, navController: NavController) {
    val activosRepository = remember { ActivosRepository() }
    val cuentasRepository = remember { CuentasRepository() }
    val portafoliosRepository = remember { PortafoliosRepository() }

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
    var totalPorcentaje by remember { mutableStateOf(0) }

    //Paso Tres
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SnackBarConColor(
                    snackbarHostState = snackbarHostState,
                    tipo = snackbarType
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Log.i("GuardarPortafolio", "Paso actual: $pasoActual")

            when (pasoActual) {
                PasoWizard.PASO_UNO -> GuardarPortafolioPasoUno(
                    modifier,
                    nombre,
                    descripcion,
                    onSiguienteClick = { nombrePortafolio, descripcionPortafolio ->
                        nombre = nombrePortafolio
                        descripcion = descripcionPortafolio
                        pasoActual = PasoWizard.PASO_DOS
                    }
                )

                PasoWizard.PASO_DOS -> {
                    LaunchedEffect(Unit) {
                        Log.i("GuardarPortafolioPasoDos", "Cargando activos...")
                        activosRepository.buscarActivos { result ->
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
                        cuentasRepository.buscarCuentas(true) { result ->
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
                            Log.i("GuardarPortafolio", "Guardando portafolio... $portafolioPorGuardar")

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
}
