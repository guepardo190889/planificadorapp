package com.example.planificadorapp.pantallas.portafolios.actualizado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.composables.activos.SeleccionarActivoDialogo
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.composiciones.GuardarComposicionModel
import com.example.planificadorapp.pantallas.portafolios.guardado.ComposicionesList
import kotlinx.coroutines.launch

@Composable
fun ActualizarPortafolioPasoDos(
    modifier: Modifier = Modifier,
    activos: List<ActivoModel>,
    composiciones: List<GuardarComposicionModel>,
    totalPorcentaje: Int,
    onAtrasClick: (List<GuardarComposicionModel>, Int) -> Unit,
    onSiguienteClick: (List<GuardarComposicionModel>, Int) -> Unit
) {
    var composicionesPasoDos by remember { mutableStateOf(composiciones) }
    var totalPorcentajePasoDos by remember { mutableIntStateOf(totalPorcentaje) }
    var mostrarDialogoActivos by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isComposicionesValidas by remember { mutableStateOf(true) }

    fun validarComposiciones(): Boolean {
        var valido = composicionesPasoDos.isNotEmpty()

        if (!valido) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("La composición debe tener al menos un activo")
            }
        } else {
            var sumaPorcentajes = 0

            for (composicion in composicionesPasoDos) {
                if (composicion.porcentaje < 1) {
                    valido = false
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Todos los porcentajes deben ser mayores a 0")
                    }
                    break
                } else {
                    sumaPorcentajes += composicion.porcentaje.toInt()
                }
            }

            if (valido) {
                if (sumaPorcentajes != 100) {
                    valido = false
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("La suma de los porcentajes debe ser igual a 100")
                    }
                }
            }
        }

        return valido
    }

    fun validarPantalla(): Boolean {
        isComposicionesValidas = validarComposiciones()
        return isComposicionesValidas
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SnackBarConColor(
                    snackbarHostState = snackbarHostState,
                    tipo = "error"
                )
            }
        },
        bottomBar = {
            BottomAppBar (
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                content = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                onAtrasClick(composicionesPasoDos, totalPorcentajePasoDos)
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Atrás"
                            )
                        }

                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                if (validarPantalla()) {
                                    onSiguienteClick(composicionesPasoDos, totalPorcentajePasoDos)
                                }
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = "Siguiente"
                            )
                        }
                    }
                }
            )
        },
        content = {
            Column(
                modifier = modifier
                    .padding(it)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Composición de activos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                ComposicionesList (
                    composiciones = composicionesPasoDos,
                    onEliminarComposicion = {
                        composicionesPasoDos = composicionesPasoDos - it
                        totalPorcentajePasoDos = composicionesPasoDos.sumOf { it.porcentaje.toInt() }
                    },
                    onPorcentajeCambiado = {
                        totalPorcentajePasoDos = composicionesPasoDos.sumOf { it.porcentaje.toInt() }
                    }
                )

                Button (
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { mostrarDialogoActivos = true }
                ) {
                    Text("Agregar activo")
                }
            }

            if (mostrarDialogoActivos) {
                val activosDisponibles = activos.filterNot { activo ->
                    composicionesPasoDos.any { it.activo == activo }
                }

                SeleccionarActivoDialogo (
                    activosDisponibles,
                    onActivoSeleccionado = { activoSeleccionado ->
                        composicionesPasoDos = composicionesPasoDos + GuardarComposicionModel(
                            activoSeleccionado
                        )
                        mostrarDialogoActivos = false
                    },
                    onDismissRequest = { mostrarDialogoActivos = false }
                )
            }
        }
    )
}