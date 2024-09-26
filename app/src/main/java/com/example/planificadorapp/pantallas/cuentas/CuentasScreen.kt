package com.example.planificadorapp.pantallas.cuentas

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.R
import com.example.planificadorapp.composables.fab.FloatingActionButtonGuardar
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.repositorios.CuentasRepository
import com.example.planificadorapp.utilerias.FormatoFecha
import com.example.planificadorapp.utilerias.FormatoMonto
import java.math.BigDecimal

/**
 * Composable que representa la pantalla de cuentas
 */
@Composable
fun Cuentas(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val cuentaRepository = CuentasRepository()
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var totalSaldos by remember { mutableStateOf(BigDecimal.ZERO) }

    val scrollState = rememberLazyListState()
    val isFabVisible by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }

    LaunchedEffect(Unit) {
        cuentaRepository.buscarCuentas(
            excluirCuentasAsociadas = false, incluirSoloCuentasNoAgrupadorasSinAgrupar = false
        ) { cuentasEncontradas ->
            cuentasEncontradas?.let {
                cuentas = it
                totalSaldos = it.filter { cuenta -> !cuenta.agrupadora } // Filtrar cuentas que no sean agrupadoras
                    .sumOf { cuenta -> cuenta.saldo }  // Sumar solo los saldos de las cuentas no agrupadoras
                Log.i("CuentasScreen", "Cuentas encontradas: ${it.size}")
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButtonGuardar(isVisible = isFabVisible,
            tooltip = "Guardar una nueva cuenta",
            onClick = { navController.navigate("cuentas/guardar") })
    }, content = { paddingValues ->
        Column(
            modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (cuentas.isEmpty()) {
                Text(
                    modifier = modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "Sin cuentas",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = scrollState
                ) {
                    itemsIndexed(cuentas) { indice, cuenta ->
                        CuentaItem(modifier, navController, cuenta)

                        // Añadir separador entre grupos de cuentas
                        val isUltimoElemento = indice == cuentas.size - 1
                        val isProximoPadre = !isUltimoElemento && cuentas[indice + 1].padre == null

                        if (isUltimoElemento || isProximoPadre) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Total: ${FormatoMonto.formato(totalSaldos)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    })
}

/**
 * Composable que muestra un ítem de cuenta
 */
@Composable
fun CuentaItem(
    modifier: Modifier, navController: NavController, cuenta: CuentaModel
) {
    val paddingStart = if (cuenta.isHija) 16.dp else 0.dp

    ListItem(modifier = modifier
        .padding(start = paddingStart)
        .clickable {
            navController.navigate("cuentas/detalle/${cuenta.id}")
        }, headlineContent = {
        Text(
            text = cuenta.nombre, style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (cuenta.agrupadora) FontWeight.Bold else FontWeight.Normal
            ), color = MaterialTheme.colorScheme.onSurface
        )
    }, supportingContent = {
        Text(
            text = ("Actualización: " + cuenta.fechaActualizacion?.let {
                FormatoFecha.formato(
                    it
                )
            }) ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }, leadingContent = {
        Icon(
            painter = painterResource(id = R.drawable.baseline_account_balance_24),
            contentDescription = "Icono de Cuenta",
            tint = MaterialTheme.colorScheme.primary
        )
    }, trailingContent = {
        Text(
            text = FormatoMonto.formato(cuenta.saldo),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (cuenta.agrupadora) FontWeight.Bold else FontWeight.Normal
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    })
}