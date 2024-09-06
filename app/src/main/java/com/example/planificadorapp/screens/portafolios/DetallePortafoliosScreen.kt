package com.example.planificadorapp.screens.portafolios

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.planificadorapp.composables.graficos.GraficaPastel

/**
 * Composable que representa la pantalla de detalle de un portafolio
 */
@Composable
fun DetallePortafoliosScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    idPortafolio: Long
) {
    Scaffold(modifier = modifier.fillMaxWidth(),
        content = {
            Column(modifier = modifier.padding(it)) {
                GraficaPastel(listOf(
                    "Acciones" to 80567.85f,
                    "AAA" to 1000.0f
                ))
            }
        })
}