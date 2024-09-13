package com.example.planificadorapp.pantallas.activos

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.modelos.activos.TransaccionActivoRequestModel
import com.example.planificadorapp.repositorios.ActivosRepository
import kotlinx.coroutines.launch

/**
 * Composable que representa la pantalla de guardado/actualización de un activo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaccionActivosScreen(modifier: Modifier, navController: NavController, activoId: Long) {
    val activosRepository = ActivosRepository()

    var activosPadre by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var activo by remember { mutableStateOf<ActivoModel?>(null) }
    var descripcionBoton by remember { mutableStateOf("Guardar") }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var activoSeleccionado by remember { mutableStateOf<ActivoModel?>(null) }
    var activoPrincipalListaDesplegada by remember { mutableStateOf(false) }
    var activoPrincipalListaHabilitada by remember { mutableStateOf(true) }

    var isNombreValido by remember { mutableStateOf(true) }
    var isActivoSeleccionadoValido by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        activosRepository.buscarActivos(true) { resultadoActivosPadres ->
            activosPadre = resultadoActivosPadres ?: emptyList()
            Log.i("ActivosScreen", "Activos padre cargados: $resultadoActivosPadres")

            if (activoId != 0L) {
                activosRepository.buscarActivoPorId(activoId) { resultadoActivoExistente ->
                    if (resultadoActivoExistente != null) {
                        Log.i("ActivosScreen", "Activo encontrado: $resultadoActivoExistente")
                        activo = resultadoActivoExistente
                        nombre = resultadoActivoExistente.nombre
                        descripcion = resultadoActivoExistente.descripcion
                        activoSeleccionado =
                            activosPadre.find { it.id == resultadoActivoExistente.padre?.id }

                        Log.i("ActivosScreen", "Activo padre encontrado: $activoSeleccionado")

                        descripcionBoton = "Actualizar"
                    }
                }

                activoPrincipalListaHabilitada = false
            }
        }
    }

    /**
     * Valida si un nombre es válido
     */
    fun validarNombre(nombre: String): Boolean {
        return nombre.isNotBlank()
    }

    /**
     * Valida la pantalla actual
     */
    fun validarPantalla(): Boolean {
        isNombreValido = validarNombre(nombre)
        isActivoSeleccionadoValido = activoSeleccionado != null

        return isNombreValido && isActivoSeleccionadoValido
    }

    /**
     * Guarda el activo en la base de datos
     */
    fun guardarActivo() {
        activosRepository.guardarActivo(
            TransaccionActivoRequestModel(
                nombre,
                descripcion,
                activoSeleccionado!!.id
            )
        ) { activoGuardado ->
            if (activoGuardado != null) {
                snackbarMessage = "Activo guardado exitosamente"
                snackbarType = "success"
            } else {
                snackbarMessage = "Error al guardar el activo"
                snackbarType = "error"
            }

            coroutineScope.launch {
                snackbarHostState.showSnackbar(snackbarMessage)

                if (activoGuardado != null) {
                    navController.navigate("activos")
                }
            }
        }
    }

    /**
     * Actualiza el activo en la base de datos
     */
    fun actualizarActivo() {
        activosRepository.actualizarActivo(
            activoId,
            TransaccionActivoRequestModel(nombre, descripcion, activoSeleccionado!!.id)
        ) {
            if (it != null) {
                snackbarMessage = "Activo actualizado exitosamente"
                snackbarType = "success"
            } else {
                snackbarMessage = "Error al actualizar el activo"
                snackbarType = "error"
            }

            coroutineScope.launch {
                snackbarHostState.showSnackbar(snackbarMessage)

                if (it != null) {
                    navController.navigate("activos")
                }
            }
        }
    }

    Scaffold(
        modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SnackBarConColor(
                    snackbarHostState = snackbarHostState,
                    tipo = snackbarType
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                if (validarPantalla()) {
                                    if (activoId == 0L) {
                                        guardarActivo()
                                    } else {
                                        actualizarActivo()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = descripcionBoton
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = activoPrincipalListaDesplegada,
                    onExpandedChange = {
                        if (activoPrincipalListaHabilitada) { //No se permite modificar el activo principal de un activo existente
                            activoPrincipalListaDesplegada = !activoPrincipalListaDesplegada
                        }
                    }) {
                    OutlinedTextField(
                        value = activoSeleccionado?.nombre ?: "",
                        onValueChange = { },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        enabled = activoPrincipalListaHabilitada,
                        readOnly = true,
                        label = {
                            Text(
                                "Selecciona un Activo Principal",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = if (activoPrincipalListaDesplegada) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        isError = !isActivoSeleccionadoValido,
                        supportingText = {
                            if (!isActivoSeleccionadoValido) {
                                Text(
                                    text = "El activo principal es requerido",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors()
                    )

                    ExposedDropdownMenu(
                        expanded = activoPrincipalListaDesplegada,
                        onDismissRequest = { activoPrincipalListaDesplegada = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        activosPadre.forEach { activo ->
                            DropdownMenuItem(
                                text = { Text(activo.nombre) },
                                onClick = {
                                    activoSeleccionado = activo
                                    activoPrincipalListaDesplegada = false
                                    isActivoSeleccionadoValido = true
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        isNombreValido = validarNombre(it)

                        if (it.length <= 52) {
                            nombre = it
                        }
                    },
                    label = { Text("Nombre", color = MaterialTheme.colorScheme.onSurface) },
                    isError = !isNombreValido,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        if (!isNombreValido) {
                            Text(
                                text = "El nombre es requerido",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Text(
                            text = "${nombre.length}/52",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        if (it.length <= 256) {
                            descripcion = it
                        }
                    },
                    label = { Text("Descripción", color = MaterialTheme.colorScheme.onSurface) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text(
                            text = "${descripcion.length}/256",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )
            }
        }
    )
}