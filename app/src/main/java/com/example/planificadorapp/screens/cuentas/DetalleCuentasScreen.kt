package com.example.planificadorapp.screens.cuentas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.utilerias.FormatoFecha
import com.example.planificadorapp.utilerias.FormatoMonto

/**
 * Composable que representa la pantalla de detalle de una cuenta
 */
@Composable
fun DetalleCuentasScreen(
    modifier: Modifier = Modifier,
    cuentaId: Long,
    navController: NavController,
) {
    val cuentaRepository = remember { CuentasRepository() }

    var cuenta by remember { mutableStateOf<CuentaModel?>(null) }

    LaunchedEffect(cuentaId) {
        cuentaRepository.buscarCuentaPorId(cuentaId) { cuentaEncontrada ->
            cuenta = cuentaEncontrada
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = { navController.navigate("cuentas/editar/${cuentaId}") },
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Actualizar Cuenta")
            }
        }
    ) { paddingValues ->
        cuenta?.let {
            Column(modifier = modifier.padding(paddingValues)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .padding(16.dp)
                ) {
                    Column(modifier = modifier.padding(16.dp)) {
                        TextoConEtiqueta("Nombre: ", it.nombre, "large", "medium")
                        TextoConEtiqueta("Saldo: ", FormatoMonto.formato(it.saldo), "large", "medium")
                        TextoConEtiqueta("Descripción: ", it.descripcion, "large", "medium")
                        TextoConEtiqueta("Fecha de actualización: ", it.fechaActualizacion?.let { FormatoFecha.formatoLargo(it)} ?: "", "large", "medium")
                    }
                }
            }
        }
    }
}
