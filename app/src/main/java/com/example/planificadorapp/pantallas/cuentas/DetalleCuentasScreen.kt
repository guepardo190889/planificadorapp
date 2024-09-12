package com.example.planificadorapp.pantallas.cuentas

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.composables.cuentas.CuentasListSimple
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.utilerias.FormatoFecha
import com.example.planificadorapp.utilerias.FormatoMonto

/**
 * Composable que representa la pantalla de detalle de una cuenta
 */
@Composable
fun DetalleCuentasScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    idCuenta: Long
) {
    val cuentaRepository = remember { CuentasRepository() }

    var cuenta by remember { mutableStateOf<CuentaModel?>(null) }
    var cuentasAgrupadas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }

    LaunchedEffect(idCuenta) {
        cuentaRepository.buscarCuentaPorId(idCuenta) { cuentaEncontrada ->
            if(cuentaEncontrada != null) {
                cuenta = cuentaEncontrada

                if(cuenta!!.agrupadora) {
                    cuentaRepository.buscarSubcuentas(cuenta!!.id) { cuentasAgrupadasEncontradas ->
                        cuentasAgrupadas = cuentasAgrupadasEncontradas ?: emptyList()
                        Log.i(
                            "DetalleCuentasScreen",
                            "Cuentas agrupadas cargadas: ${cuentasAgrupadas.size}"
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { navController.navigate("cuentas/editar/${idCuenta}") },
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
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(modifier = modifier) {
                        TextoConEtiqueta("Nombre: ", it.nombre, "large", "medium")
                        TextoConEtiqueta(
                            "Saldo: ",
                            FormatoMonto.formato(it.saldo),
                            "large",
                            "medium"
                        )
                        TextoConEtiqueta("Descripción: ", it.descripcion, "large", "medium")
                        TextoConEtiqueta(
                            "Fecha de actualización: ",
                            it.fechaActualizacion?.let { FormatoFecha.formatoLargo(it) } ?: "",
                            "large",
                            "medium")
                    }
                }

                Text(
                    modifier = modifier.fillMaxWidth().padding(16.dp),
                    text = "Cuentas Agrupadas:",
                    style = MaterialTheme.typography.titleMedium)
                CuentasListSimple(modifier, cuentasAgrupadas)
            }
        }
    }
}