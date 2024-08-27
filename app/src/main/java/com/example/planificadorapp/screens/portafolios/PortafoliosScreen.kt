package com.example.planificadorapp.screens.portafolios

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.R
import com.example.planificadorapp.configuracion.Ruta
import com.example.planificadorapp.modelos.PortafolioModel
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
            Log.i("Portafolios", "Portafolios encontrados: $result")
            portafolios = result ?: emptyList()
            isLoading = false
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Ruta.PORTAFOLIOS_GUARDAR.ruta)
                },
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFF88C6F5)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Guardar Portafolio")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            PortafoliosList(portafolios, navController, Modifier.padding(16.dp))
        }
    }
}

@Composable
fun PortafoliosList(
    portafolios: List<PortafolioModel>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(portafolios) { portafolio ->
            PortafolioItem(portafolio, navController)
            HorizontalDivider(color = Color(0xFFC2C2C2))
        }
    }
}

@Composable
fun PortafolioItem(portafolio: PortafolioModel, navController: NavController) {
    ListItem(
        headlineContent = {
            Text(text = portafolio.nombre, color = Color(0xFF000000))
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_balance_24),
                contentDescription = "Localized description",
                tint = Color(0xFF88C6F5)
            )
        },
        trailingContent = { Text("$100,000.00", color = Color(0xFFC2C2C2)) },
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                Log.i("Mensaje", "Aquí se debe ir al detalle de un portafolio")
            }
    )
}