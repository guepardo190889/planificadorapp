package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.composables.textfield.OutlinedTextFieldBase
import com.example.planificadorapp.composables.navegacion.BarraNavegacionInferior
import com.example.planificadorapp.utilerias.validadores.PortafolioValidador

/**
 * Composable que muestra los campos generales de un portafolio
 */
@OptIn(ExperimentalMaterial3Api::class)
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
            EncabezadoPortafolio(titulo = "Generales")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextFieldBase(modifier = Modifier.fillMaxWidth(),
                value = nombre,
                label = "Nombre",
                isError = !isNombreValido,
                errorMessage = if (!isNombreValido) "El nombre es requerido" else null,
                supportingText = "Ingrese el nombre del portafolio (máximo 20 caracteres)",
                maxLength = 20,
                onValueChange = { nombreActualizado ->
                    isNombreValido = PortafolioValidador.validarNombre(nombreActualizado)
                    onNombreChange(nombreActualizado)
                })

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldBase(
                value = descripcion,
                label = "Descripción",
                supportingText = "Descripción opcional (máximo 256 caracteres)",
                maxLength = 256,
                onValueChange = onDescripcionChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}