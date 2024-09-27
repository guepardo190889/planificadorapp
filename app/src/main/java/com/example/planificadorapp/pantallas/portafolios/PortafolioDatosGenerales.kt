package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.composables.textfield.OutlinedTextFieldBase
import com.example.planificadorapp.utilerias.validadores.PortafolioValidador

/**
 * Composable que muestra los campos generales de un portafolio
 */
@Composable
fun PortafolioDatosGenerales(
    modifier: Modifier = Modifier,
    nombre: String,
    descripcion: String,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onSiguienteClick: () -> Unit
) {
    var isNombreValido by remember { mutableStateOf(true) }

    val nombreFocusRequester = remember { FocusRequester() }
    val descripcionFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        nombreFocusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(bottomBar = {
        BarraNavegacionInferior(onSiguienteClick = {
            Log.i("PortafolioDatosGenerales", "isNombreValido: $isNombreValido")
            isNombreValido = PortafolioValidador.validarNombre(nombre)

            if (isNombreValido) {
                onSiguienteClick()
            }
        })
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            EncabezadoPortafolio(
                titulo = "Generales",
                descripcion = "Datos generales del portafolio"
            )

            OutlinedTextFieldBase(modifier = Modifier.fillMaxWidth(),
                value = nombre,
                label = "Nombre",
                isError = !isNombreValido,
                errorMessage = "El nombre es requerido",
                focusRequester = nombreFocusRequester,
                supportingText = "Ingrese el nombre del portafolio (máximo 20 caracteres)",
                maxLength = 20,
                onValueChange = { nombreActualizado ->
                    isNombreValido = PortafolioValidador.validarNombre(nombreActualizado)
                    onNombreChange(nombreActualizado)
                },
                onNextAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                })

            OutlinedTextFieldBase(
                modifier = Modifier.fillMaxWidth(),
                value = descripcion,
                label = "Descripción",
                supportingText = "Descripción opcional (máximo 256 caracteres)",
                focusRequester = descripcionFocusRequester,
                maxLength = 256,
                singleLine = false,
                maxLines = 3,
                onValueChange = onDescripcionChange
            )
        }
    }
}