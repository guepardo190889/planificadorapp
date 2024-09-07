package com.example.planificadorapp.screens.cuentas

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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

    LaunchedEffect(Unit) {
        cuentaRepository.buscarCuentas(false, false) { cuentasEncontradas ->
            cuentas = cuentasEncontradas ?: emptyList()

            Log.i("Cuentas", "Cuentas encontradas: $cuentasEncontradas")
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
        }
    ) { paddingValues ->
        Column(modifier.padding(paddingValues)) {
            CuentasList(modifier, cuentas, navController)
        }
    }
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
            CuentaItem(modifier, cuenta, navController)
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
    cuenta: CuentaModel,
    navController: NavController
) {
    ListItem(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("cuentas/detalle/${cuenta.id}")
            },
        headlineContent = {
            Text(text = cuenta.nombre)
        },
        supportingContent = { Text(FormatoMonto.formato(cuenta.saldo)) },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_balance_24),
                contentDescription = "Localized description",
            )
        },
        trailingContent = {
            Text(cuenta.fechaActualizacion?.let { FormatoFecha.formato(it) } ?: "")
        }
    )
}


