package com.example.planificadorapp.pantallas.portafolios

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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

/**
 * Composable que representa la pantalla de portafolios
 */
@Composable
fun Portafolios(navController: NavController, modifier: Modifier = Modifier) {
    val portafolioRepository = remember { PortafoliosRepository() }
    var portafolios by remember { mutableStateOf<List<PortafolioModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = navController) {
        portafolioRepository.buscarPortafolios { result ->
            portafolios = result ?: emptyList()
            Log.i("PortafoliosScreen", "Portafolios encontrados: ${portafolios.size}")

            isLoading = false
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
        content = {
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {
                PortafoliosList(modifier, navController, portafolios)
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
    LazyColumn(modifier.padding(16.dp)) {
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
                contentDescription = "Localized description",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = { Text("$100,000.00", color = MaterialTheme.colorScheme.secondary) }
    )
}