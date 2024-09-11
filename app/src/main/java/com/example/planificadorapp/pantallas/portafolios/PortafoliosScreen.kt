package com.example.planificadorapp.pantallas.portafolios

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
import com.example.planificadorapp.modelos.portafolios.PortafolioModel
import com.example.planificadorapp.navegacion.Ruta
import com.example.planificadorapp.repositorios.PortafoliosRepository
import com.example.planificadorapp.utilerias.FormatoMonto
import java.math.BigDecimal

/**
 * Composable que representa la pantalla de portafolios
 */
@Composable
fun Portafolios(modifier: Modifier, navController: NavController) {
    val portafolioRepository = PortafoliosRepository()
    var portafolios by remember { mutableStateOf<List<PortafolioModel>>(emptyList()) }
    var totalSaldos by remember { mutableStateOf<BigDecimal>(BigDecimal.ZERO) }

    LaunchedEffect(key1 = navController) {
        portafolioRepository.buscarPortafolios { result ->
            portafolios = result ?: emptyList()
            Log.i("PortafoliosScreen", "Portafolios encontrados: ${portafolios.size}")

            totalSaldos = portafolios.sumOf { it.saldoTotal }
            Log.i("PortafoliosScreen", "Total de saldos: $totalSaldos")
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    navController.navigate(Ruta.PORTAFOLIOS_GUARDAR.ruta)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Guardar Portafolio")
            }
        },
        content = { paddingValues ->
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                PortafoliosList(modifier, navController, portafolios)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Total: ${FormatoMonto.formato(totalSaldos)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}

/**
 * Composable que muestra la lista de portafolios
 */
@Composable
fun PortafoliosList(
    modifier: Modifier = Modifier,
    navController: NavController,
    portafolios: List<PortafolioModel>
) {
    LazyColumn(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(portafolios) { portafolio ->
            PortafolioItem(modifier, navController, portafolio)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        }
    }
}

/**
 * Composable que muestra un item de portafolio
 */
@Composable
fun PortafolioItem(
    modifier: Modifier,
    navController: NavController,
    portafolio: PortafolioModel
) {
    ListItem(
        modifier = modifier
            .clickable {
                navController.navigate("portafolios/detalle/${portafolio.id}")
            },
        headlineContent = {
            Text(text = portafolio.nombre, color = MaterialTheme.colorScheme.onSurface)
        },
        supportingContent = {
            Text(portafolio.descripcion, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.outline_work_24),
                contentDescription = "Icono de portafolio",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            Text(
                text = FormatoMonto.formato(portafolio.saldoTotal),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    )
}