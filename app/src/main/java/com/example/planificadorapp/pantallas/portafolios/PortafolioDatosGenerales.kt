package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.OutlinedTextFieldBase
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
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

    Scaffold(bottomBar = {
        BarraNavegacionInferior(
            onSiguienteClick = {
                Log.i("PortafolioDatosGenerales", "isNombreValido: $isNombreValido")
                isNombreValido = PortafolioValidador.validarNombre(nombre)

                if (isNombreValido) {
                    onSiguienteClick()
                }
            })
    }, content = { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            EncabezadoPortafolio(titulo = "Generales")

            OutlinedTextFieldBase(
                value = nombre,
                label = "Nombre",
                isError = !isNombreValido,
                errorMessage = "El nombre es requerido",
                supportingText = null,
                maxLength = 20,
                onValueChange = { nombreActualizado ->
                    isNombreValido = PortafolioValidador.validarNombre(nombreActualizado)
                    onNombreChange(nombreActualizado)
                },
            )

            OutlinedTextFieldBase(
                value = descripcion,
                label = "Descripción",
                maxLength = 256,
                onValueChange = onDescripcionChange
            )
        }
    })
}