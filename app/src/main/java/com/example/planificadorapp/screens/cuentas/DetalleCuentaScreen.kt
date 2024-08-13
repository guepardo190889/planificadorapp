package com.example.planificadorapp.screens.cuentas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.repositorios.CuentaRepository

@Composable
fun DetalleCuentaScreen(navController: NavController, cuentaId: Long) {
    var cuenta by remember { mutableStateOf<CuentaModel?>(null) }
    val cuentaRepository = remember { CuentaRepository() }

    LaunchedEffect(cuentaId) {
        cuentaRepository.buscarCuentaPorId(cuentaId) { result ->
            cuenta = result
        }
    }

    cuenta?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Detalles de la Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Nombre: ${it.nombre}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Descripción: ${it.descripcion}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Saldo: ${it.saldo}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Fecha de Actualización: ${it.fechaActualizacion}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}