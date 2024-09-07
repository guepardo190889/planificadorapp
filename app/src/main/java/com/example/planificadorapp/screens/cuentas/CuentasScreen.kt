package com.example.planificadorapp.screens.cuentas

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.R
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.utilerias.FormatoFecha
import com.example.planificadorapp.utilerias.FormatoMonto

/**
 * Composable que representa la pantalla de cuentas
 */
@Composable
fun Cuentas(modifier: Modifier = Modifier, navController: NavController) {
    val cuentaRepository = remember { CuentasRepository() }
    var cuentas by remember {
        mutableStateOf<List<CuentaModel>>(
            emptyList()
        )
    }
    var totalSaldos by remember { mutableStateOf(0.00) }

    LaunchedEffect(Unit) {
        cuentaRepository.buscarCuentas(false, false) { cuentasEncontradas ->
            cuentas = cuentasEncontradas ?: emptyList()
            Log.i("CuentasScreen", "Cuentas encontradas: ${cuentasEncontradas!!.size}")

            for (cuenta in cuentas) {
                totalSaldos = totalSaldos.toDouble() + cuenta.saldo
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("cuentas/guardar") },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Guardar Cuenta")
            }
        },
        content = {
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {
                CuentasList(modifier, cuentas, navController)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Total: ${FormatoMonto.formato(totalSaldos)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    )
}

/**
 * Composable que muestra la lista de cuentas
 */
@Composable
fun CuentasList(
    modifier: Modifier = Modifier,
    cuentas: List<CuentaModel>,
    navController: NavController
) {
    LazyColumn(modifier.padding(16.dp)) {
        items(cuentas) { cuenta ->
            CuentaItem(modifier, navController, cuenta)
            HorizontalDivider()
        }
    }
}

/**
 * Composable que muestra un ítem de cuenta
 */
@Composable
fun CuentaItem(
    modifier: Modifier,
    navController: NavController,
    cuenta: CuentaModel
) {
    ListItem(
        modifier = modifier
            .clickable {
                navController.navigate("cuentas/detalle/${cuenta.id}")
            },
        headlineContent = {
            Text(text = cuenta.nombre)
        },
        supportingContent = {
            Text(cuenta.fechaActualizacion?.let { FormatoFecha.formato(it) } ?: "")
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_balance_24),
                contentDescription = "Localized description",
            )
        },
        trailingContent = {
            Text(FormatoMonto.formato(cuenta.saldo))
        }
    )
}