package com.example.planificadorapp.screens.portafolios

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.R
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.modelos.PortafolioModel
import com.example.planificadorapp.repositorios.PortafoliosRepository
import java.text.NumberFormat
import java.util.Locale

/**
 * Composable que representa la pantalla de portafolios
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Portafolios(navController: NavController, modifier: Modifier = Modifier) {
    val portafolioRepository = remember { PortafoliosRepository() }
    var portafolios by remember { mutableStateOf<List<PortafolioModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        portafolioRepository.buscarPortafolios { result ->
            Log.i("Portafolios", "Portafolios encontrados: $result")
            portafolios = result ?: emptyList()
            isLoading = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { Log.i("Mensaje", "Aquí se debe comenzar el wizard para crear un nuevo portafolio") },
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFF88C6F5)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Guardar Portafolio")
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SnackBarConColor(
                    snackbarHostState = snackbarHostState,
                    tipo = snackbarType
                )
            }
        },
        modifier = modifier.fillMaxSize()
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
    Column(modifier = modifier.padding(16.dp)) {
        portafolios.forEach { portafolio ->
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

fun formatCurrency(amount: Double): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    return format.format(amount)
}