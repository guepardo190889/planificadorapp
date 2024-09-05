package com.example.planificadorapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ReportesScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Pantalla de Reportes")
    }
}

@Composable
fun ConfiguracionScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Pantalla de Configuración")
    }
}