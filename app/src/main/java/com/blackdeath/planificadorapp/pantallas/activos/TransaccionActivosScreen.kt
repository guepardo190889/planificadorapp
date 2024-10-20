package com.blackdeath.planificadorapp.pantallas.activos

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.blackdeath.planificadorapp.composables.activos.ActivosDropDonw
import com.blackdeath.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.blackdeath.planificadorapp.composables.snackbar.SnackBarBase
import com.blackdeath.planificadorapp.composables.snackbar.SnackBarManager
import com.blackdeath.planificadorapp.composables.snackbar.SnackBarTipo
import com.blackdeath.planificadorapp.composables.textfield.OutlinedTextFieldBase
import com.blackdeath.planificadorapp.modelos.activos.ActivoModel
import com.blackdeath.planificadorapp.modelos.activos.TransaccionActivoRequestModel
import com.blackdeath.planificadorapp.navegacion.Ruta
import com.blackdeath.planificadorapp.repositorios.ActivosRepository

/**
 * Composable que representa la pantalla de guardado/actualización de un activo
 */
@Composable
fun TransaccionActivosScreen(modifier: Modifier, navController: NavController, activoId: Long) {
    val activosRepository = ActivosRepository()

    var activosPadre by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }
    var activo by remember { mutableStateOf<ActivoModel?>(null) }

    var isTransaccionGuardar by remember { mutableStateOf(true) }
    var descripcionBoton by remember { mutableStateOf("Guardar") }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var activoSeleccionado by remember { mutableStateOf<ActivoModel?>(null) }

    var isNombreValido by remember { mutableStateOf(true) }
    var isActivoSeleccionadoValido by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarManager = remember { SnackBarManager(coroutineScope, snackbarHostState) }

    val nombreFocusRequester = remember { FocusRequester() }
    val descripcionFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var isTransaccionando by remember { mutableStateOf(false) }

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
     * Guarda o actualiza el activo
     */
    fun transaccionarMovimiento() {
        isTransaccionando = true

        val transaccionModel = TransaccionActivoRequestModel(
            nombre, descripcion, activoSeleccionado!!.id
        )

        if (isTransaccionGuardar) {
            activosRepository.guardarActivo(
                transaccionModel
            ) { activoGuardado ->
                if (activoGuardado != null) {
                    snackBarManager.mostrar(
                        "Activo guardado exitosamente", SnackBarTipo.SUCCESS
                    ) {
                        isTransaccionando = false
                        navController.navigate(Ruta.ACTIVOS.ruta)
                    }
                } else {
                    snackBarManager.mostrar("Error al guardar el activo", SnackBarTipo.ERROR) {
                        isTransaccionando = false
                    }
                }
            }
        } else {
            activosRepository.actualizarActivo(
                activoId, transaccionModel
            ) { activoActualizado ->
                if (activoActualizado != null) {
                    snackBarManager.mostrar(
                        "Activo actualizado exitosamente", SnackBarTipo.SUCCESS
                    ) {
                        isTransaccionando = false
                        navController.navigate(Ruta.ACTIVOS.ruta)
                    }
                } else {
                    snackBarManager.mostrar("Error al actualizar el activo", SnackBarTipo.ERROR) {
                        isTransaccionando = false
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        activosRepository.buscarActivos(true) { resultadoActivosPadres ->
            activosPadre = resultadoActivosPadres ?: emptyList()
            Log.i("ActivosScreen", "Activos padre cargados: ${activosPadre.size}")

            if (activoId != 0L) {
                activosRepository.buscarActivoPorId(activoId) { resultadoActivoExistente ->
                    if (resultadoActivoExistente != null) {
                        Log.i("ActivosScreen", "Activo encontrado: $resultadoActivoExistente")

                        activo = resultadoActivoExistente
                        nombre = resultadoActivoExistente.nombre
                        descripcion = resultadoActivoExistente.descripcion ?: ""
                        activoSeleccionado =
                            activosPadre.find { it.id == resultadoActivoExistente.padre?.id }

                        Log.i("ActivosScreen", "Activo padre encontrado: $activoSeleccionado")
                    }
                }

                isTransaccionGuardar = false
                descripcionBoton = "Actualizar"
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxWidth(), snackbarHost = {
        SnackBarBase(
            snackbarHostState = snackbarHostState, snackBarManager = snackBarManager
        )
    }, bottomBar = {
        BarraNavegacionInferior(isTransaccionGuardar = isTransaccionGuardar, onTransaccionClick = {
            if (validarPantalla()) {
                if (validarPantalla()) {
                    transaccionarMovimiento()
                }
            }
        })
    }, content = { paddingValues ->
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                ActivosDropDonw(modifier = Modifier.fillMaxWidth(),
                    etiqueta = "Selecciona un Activo Principal",
                    activos = activosPadre,
                    isHabilitado = isTransaccionGuardar,
                    isError = !isActivoSeleccionadoValido,
                    mensajeError = "El activo principal es requerido",
                    activoSeleccionado = activoSeleccionado,
                    onActivoSeleccionado = {
                        isActivoSeleccionadoValido = true
                        activoSeleccionado = it
                    })

                OutlinedTextFieldBase(value = nombre,
                    label = "Nombre",
                    isError = !isNombreValido,
                    errorMessage = "El nombre es requerido",
                    focusRequester = nombreFocusRequester,
                    supportingText = "Ingrese el nombre del activo (máximo 52 caracteres)",
                    maxLength = 52,
                    singleLine = false,
                    maxLines = 2,
                    onValueChange = {
                        isNombreValido = validarNombre(it)
                        nombre = it
                    },
                    onNextAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })

                OutlinedTextFieldBase(value = descripcion,
                    label = "Descripción",
                    focusRequester = descripcionFocusRequester,
                    supportingText = "Descripción opcional (máximo 256 caracteres)",
                    maxLength = 256,
                    singleLine = false,
                    maxLines = 3,
                    onValueChange = {
                        descripcion = it
                    })
            }

            // Si está guardando, mostramos un indicador de carga que bloquea toda la pantalla
            if (isTransaccionando) {
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