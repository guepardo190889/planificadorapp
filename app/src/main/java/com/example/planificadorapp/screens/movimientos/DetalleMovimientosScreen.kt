package com.example.planificadorapp.screens.movimientos

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.planificadorapp.composables.graficos.GraficaPastel

/**
 * Composable que representa la pantalla de detalle de un movimiento
 */
@Composable
fun DetalleMovimientosScreen(
    modifier: Modifier,
    navController: NavController,
    idMovimiento: Long
) {
    GraficaPastel(listOf(
        "Acciones" to 80567.85f,
        "AAA" to 1000.0f
    ))
}