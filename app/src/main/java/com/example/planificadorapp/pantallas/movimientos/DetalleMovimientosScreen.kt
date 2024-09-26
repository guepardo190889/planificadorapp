package com.example.planificadorapp.pantallas.movimientos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.planificadorapp.composables.fab.FloatingActionButtonActualizar
import com.example.planificadorapp.modelos.movimientos.MovimientoModel
import com.example.planificadorapp.repositorios.MovimientosRepository
import com.example.planificadorapp.utilerias.FormatoFecha
import com.example.planificadorapp.utilerias.FormatoMonto

/**
 * Composable que representa la pantalla de detalle de un movimiento
 */
@Composable
fun DetalleMovimientosScreen(
    modifier: Modifier, navController: NavController, idMovimiento: Long
) {
    val movimientoRepository = MovimientosRepository()

    var movimiento by remember { mutableStateOf<MovimientoModel?>(null) }

    LaunchedEffect(idMovimiento) {
        movimientoRepository.buscarMovimientoPorId(idMovimiento) { movimientoEncontrado ->
            movimiento = movimientoEncontrado
        }
    }

    Scaffold(modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButtonActualizar(tooltip = "Actualizar el movimiento", onClick = {
            navController.navigate("movimientos/editar/${movimiento?.id}")
        })
    }, content = { paddingValues ->
        movimiento?.let {
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                Card(
                    modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(modifier.padding(16.dp)) {
                        TextoConEtiqueta("Cuenta: ", it.nombreCuenta, "large", "medium")
                        TextoConEtiqueta("Tipo: ", it.tipo.name, "large", "medium")
                        TextoConEtiqueta(
                            "Monto: ", FormatoMonto.formato(it.monto), "large", "medium"
                        )
                        TextoConEtiqueta(
                            "Fecha: ", FormatoFecha.formatoLargo(it.fecha), "large", "medium"
                        )
                        TextoConEtiqueta("Concepto: ", it.concepto ?: "", "large", "medium")
                        TextoConEtiqueta(
                            "Descripción: ", it.descripcion ?: "", "large", "medium"
                        )
                    }
                }
            }
        }
    })

}