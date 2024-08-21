package com.example.planificadorapp.screens.portafolios.guardado

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.planificadorapp.modelos.ActivoModel
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.repositorios.ActivosRepository
import com.example.planificadorapp.repositorios.CuentasRepository

/**
 * Composable que representa la pantalla de guardado de un portafolio
 */
@Composable
fun GuardarPortafolio(modifier: Modifier = Modifier, navController: NavController) {
    val activosRepository = remember { ActivosRepository() }
    val cuentasRepository = remember {CuentasRepository()}

    var pasoActual: PasoWizard by remember { mutableStateOf(PasoWizard.PASO_UNO) }

    //Paso Uno
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    //Paso Dos
    var activos by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var activosSeleccionados by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var totalPorcentaje by remember { mutableStateOf(0) }

    //Paso Tres
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }

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
                activosSeleccionados,
                totalPorcentaje,
                onAtrasClick = { activosSeleccionadosPortafolio, totalPorcentajePortafolio ->
                    activosSeleccionados = activosSeleccionadosPortafolio
                    totalPorcentaje = totalPorcentajePortafolio
                    pasoActual = PasoWizard.PASO_UNO
                },
                onSiguienteClick = { activosSeleccionadosPortafolio, totalPorcentajePortafolio ->
                    activosSeleccionados = activosSeleccionadosPortafolio
                    totalPorcentaje = totalPorcentajePortafolio
                    pasoActual = PasoWizard.PASO_TRES
                }
            )
        }

        PasoWizard.PASO_TRES -> {
            LaunchedEffect(Unit) {
                Log.i("GuardarPortafolioPasoDos", "Cargando cuentas...")
                cuentasRepository.buscarCuentas { result ->
                    cuentas = result ?: emptyList()
                }
            }

            GuardarPortafolioPasoTres(
                activosSeleccionados,
                onAtrasClick = {
                    pasoActual = PasoWizard.PASO_DOS
                },
                onSiguienteClick = {
                    pasoActual = PasoWizard.PASO_RESUMEN
                }
            )
        }
        PasoWizard.PASO_RESUMEN -> TODO()
    }
}
