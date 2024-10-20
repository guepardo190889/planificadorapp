package com.blackdeath.planificadorapp.pantallas.portafolios

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.blackdeath.planificadorapp.composables.EncabezadoScreen
import com.blackdeath.planificadorapp.composables.activos.SeleccionarActivoDialogo
import com.blackdeath.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.blackdeath.planificadorapp.modelos.activos.ActivoModel
import com.blackdeath.planificadorapp.modelos.composiciones.GuardarComposicionModel

/**
 * Composable que muestra la distribución de los activos en un portafolio
 */
@Composable
fun PortafolioDistribucionActivos(
    modifier: Modifier = Modifier,
    activos: List<ActivoModel>,
    composiciones: List<GuardarComposicionModel>,
    onAgregarComposicion: (GuardarComposicionModel) -> Unit,
    onEliminarComposicion: (GuardarComposicionModel) -> Unit,
    onPorcentajeCambiado: (GuardarComposicionModel, Int) -> Unit,
    onAtrasClick: () -> Unit,
    onSiguienteClick: () -> Unit
) {
    var totalPorcentaje by remember { mutableIntStateOf(0) }
    var isComposicionesValidas by remember { mutableStateOf(true) }
    var mensajeErrorComposiciones by remember { mutableStateOf("") }
    var mostrarDialogoActivos by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    var isNuevoActivoAgregado by remember { mutableStateOf(false) }

    LaunchedEffect(composiciones) {
        totalPorcentaje = composiciones.sumOf { it.porcentaje.toInt() }
    }

    LaunchedEffect(isNuevoActivoAgregado) {
        if (isNuevoActivoAgregado) {
            scrollState.animateScrollTo(scrollState.maxValue)
            isNuevoActivoAgregado = false
        }
    }

    /**
     * Función que valida si las composiciones son válidas
     */
    fun validarComposiciones() {
        isComposicionesValidas = true

        when {
            composiciones.isEmpty() -> {
                isComposicionesValidas = false
                mensajeErrorComposiciones = "El portafolio debe tener al menos un activo"
            }

            composiciones.any { it.porcentaje < 1 } -> {
                isComposicionesValidas = false
                mensajeErrorComposiciones = "Todos los porcentajes deben ser mayores a 0"
            }

            totalPorcentaje != 100 -> {
                isComposicionesValidas = false
                mensajeErrorComposiciones = "La suma de los porcentajes debe ser igual a 100"
            }
        }
    }

    Scaffold(bottomBar = {
        BarraNavegacionInferior(onAtrasClick = onAtrasClick, onSiguienteClick = {
            validarComposiciones()
            if (isComposicionesValidas) onSiguienteClick()
        })
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            EncabezadoScreen(
                titulo = "Distribución de activos",
                descripcion = "Porcentaje de los activos dentro del portafolio"
            )

            Column(
                modifier = Modifier
                    .then(
                        if (!isComposicionesValidas) Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.error,
                            shape = MaterialTheme.shapes.small
                        ) else Modifier
                    )
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                composiciones.forEach { composicion ->
                    key(composicion.activo.id) {
                        ComposicionItem(
                            composicion = composicion,
                            onEliminarComposicion = onEliminarComposicion,
                            onPorcentajeCambiado = onPorcentajeCambiado
                        )
                    }

                    HorizontalDivider()
                }
            }

            if (!isComposicionesValidas) {
                Text(
                    text = mensajeErrorComposiciones,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            SeccionPorcentajeTotalDistribucionActivos(totalPorcentaje)

            Button(
                onClick = { mostrarDialogoActivos = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Agregar activo")
            }

            if (mostrarDialogoActivos) {
                val activosDisponibles = activos.filterNot { activo ->
                    composiciones.any { it.activo == activo }
                }

                SeleccionarActivoDialogo(activos = activosDisponibles,
                    onActivoSeleccionado = { activoSeleccionado ->
                        onAgregarComposicion(GuardarComposicionModel(activoSeleccionado))
                        mostrarDialogoActivos = false

                        isNuevoActivoAgregado = true
                    },
                    onDismissRequest = { mostrarDialogoActivos = false })
            }
        }
    }
}

@Composable
fun ComposicionItem(
    composicion: GuardarComposicionModel,
    onEliminarComposicion: (GuardarComposicionModel) -> Unit,
    onPorcentajeCambiado: (GuardarComposicionModel, Int) -> Unit,
) {
    var posicionSlider by remember { mutableFloatStateOf(composicion.porcentaje) }
    var porcentajeTexto by remember { mutableStateOf(posicionSlider.toInt().toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = composicion.activo.nombre,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { onEliminarComposicion(composicion) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Activo")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = posicionSlider, onValueChange = {
                    posicionSlider = it
                    porcentajeTexto = it.toInt().toString()
                    onPorcentajeCambiado(composicion, it.toInt())
                }, valueRange = 0f..100f, modifier = Modifier.fillMaxWidth(0.7f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = porcentajeTexto,
                onValueChange = { nuevoTexto ->
                    //Cuando se borra el único número
                    if (nuevoTexto.isEmpty()) {
                        porcentajeTexto = "0"
                    } else if (nuevoTexto.length == 1) {
                        porcentajeTexto = nuevoTexto
                    } else if (nuevoTexto.length == 2) {
                        //primer número con cursor a la izquierda (ejemplo: 01)
                        porcentajeTexto = if (nuevoTexto[1] == '0' && (porcentajeTexto == "0" || porcentajeTexto.isEmpty())) {
                            nuevoTexto[0].toString()
                        }
                        //primer número con cursor a la derecha (ejemplo: 02)
                        else if (nuevoTexto[0] == '0' && (porcentajeTexto == "0" || porcentajeTexto.isEmpty())) {
                            nuevoTexto[1].toString()
                        } else {
                            nuevoTexto
                        }
                    } else if (nuevoTexto.length == 3) {
                        porcentajeTexto = nuevoTexto
                    }

                    val nuevoPorcentaje = porcentajeTexto.toIntOrNull() ?: 0

                    if (nuevoPorcentaje in 0..100) {
                        posicionSlider = nuevoPorcentaje.toFloat()
                        onPorcentajeCambiado(composicion, nuevoPorcentaje)
                    }
                },
                modifier = Modifier
                    .width(56.dp)
                    .height(46.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Text(
                text = "%",
                modifier = Modifier.padding(2.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    HorizontalDivider()
}

@Composable
fun SeccionPorcentajeTotalDistribucionActivos(totalPorcentaje: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Total: ${totalPorcentaje}%",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}