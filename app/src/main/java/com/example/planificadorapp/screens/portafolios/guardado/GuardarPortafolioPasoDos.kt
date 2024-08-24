package com.example.planificadorapp.screens.portafolios.guardado

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.modelos.ActivoModel
import com.example.planificadorapp.modelos.GuardarComposicionModel
import kotlinx.coroutines.launch

/**
 * Composable que representa la pantalla del segundo paso en el guardado de un portafolio.
 * El segundo paso consiste en seleccionar los activos que se agregarán al portafolio.
 */
@Composable
fun GuardarPortafolioPasoDos(
    modifier: Modifier = Modifier,
    activos: List<ActivoModel>,
    composiciones: List<GuardarComposicionModel>,
    totalPorcentaje: Int,
    onAtrasClick: (List<GuardarComposicionModel>, Int) -> Unit,
    onSiguienteClick: (List<GuardarComposicionModel>, Int) -> Unit
) {
    var composicionesPasoDos by remember {
        mutableStateOf(
            composiciones
        )
    }
    var mostrarDialogoActivos by remember { mutableStateOf(false) }
    var totalPorcentajePasoDos by remember { mutableStateOf(totalPorcentaje) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isComposicionesValidas by remember { mutableStateOf(true) }

    /**
     * Función que valida si las composiciones son válidas
     */
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

                    break;
                } else {
                    sumaPorcentajes += composicion.porcentaje.toInt()
                }
            }

            Log.i("GuardarPortafolioPasoDos", "Suma de porcentajes: $sumaPorcentajes")

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

    /**
     * Función que valida la pantalla actual
     */
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
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
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
                            onClick = {
                                if (validarPantalla()) {
                                    onSiguienteClick(
                                        composicionesPasoDos,
                                        totalPorcentajePasoDos
                                    )
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "Composición de activos", style = MaterialTheme.typography.headlineMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            ComposicionesList(
                composiciones = composicionesPasoDos,
                onEliminarComposicion = {
                    composicionesPasoDos = composicionesPasoDos - it
                    totalPorcentajePasoDos = sumarPorcentajes(composicionesPasoDos)
                },
                onPorcentajeCambiado = {
                    totalPorcentajePasoDos = sumarPorcentajes(composicionesPasoDos)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total: ${totalPorcentajePasoDos.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = {
                    mostrarDialogoActivos = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Agregar")
            }
        }

        if (mostrarDialogoActivos) {
            val activosDisponibles = activos.filterNot { activo ->
                composicionesPasoDos.any { it.activo == activo }
            }

            SeleccionarActivoDialogo(
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
}

/**
 * Composable que muestra una lista de activos seleccionados
 */
@Composable
fun ComposicionesList(
    composiciones: List<GuardarComposicionModel>,
    modifier: Modifier = Modifier,
    onEliminarComposicion: (GuardarComposicionModel) -> Unit,
    onPorcentajeCambiado: () -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {
        composiciones.forEach { composicion ->
            ComposicionItem(
                composicion,
                onEliminarComposicion,
                onPorcentajeCambiado
            )
            HorizontalDivider()
        }
    }
}

/**
 * Composable que muestra la lista de composiciones
 */
@Composable
fun ComposicionItem(
    composicion: GuardarComposicionModel,
    onEliminarComposicion: (GuardarComposicionModel) -> Unit,
    onPorcentajeCambiado: () -> Unit
) {
    var posicionSlider by remember { mutableStateOf(composicion.porcentaje) }

    ListItem(
        headlineContent = {
            Text(text = composicion.activo.nombre, style = MaterialTheme.typography.bodyLarge)
        },
        supportingContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    value = posicionSlider,
                    onValueChange = {
                        posicionSlider = it
                        composicion.porcentaje = it
                        onPorcentajeCambiado()
                    },
                    valueRange = 0f..100f
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .width(56.dp)
                        .height(46.dp),
                    value = posicionSlider.toInt().toString(),
                    onValueChange = {
                        val intValue = it.toIntOrNull()?.coerceIn(0, 100) ?: posicionSlider.toInt()
                        posicionSlider = intValue.toFloat()
                        composicion.porcentaje = posicionSlider
                        onPorcentajeCambiado()
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Text(modifier = Modifier.padding(2.dp), text = "%")
            }
        },
        trailingContent = {
            IconButton(onClick = {
                onEliminarComposicion(composicion)
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Activo")
            }
        }
    )
    HorizontalDivider()
}

fun sumarPorcentajes(activos: List<GuardarComposicionModel>): Int {
    return activos.sumOf { it.porcentaje.toDouble() }.toInt()
}