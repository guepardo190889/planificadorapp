package com.example.planificadorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.Cuenta
import com.example.planificadorapp.repositorios.CuentaRepository
import com.example.planificadorapp.servicios.ApiClient
import com.example.planificadorapp.ui.theme.PlanificadorappTheme
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanificadorappTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val cuentaRepository = remember { CuentaRepository() }
    var cuentas by remember { mutableStateOf<List<Cuenta>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        cuentaRepository.buscarCuentas { result ->
            Log.i("MainScreen", "Cuentas encontradas: $result")
            cuentas = result ?: emptyList()
            isLoading = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { NavigationDrawer() }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cuentas") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* TODO: Acción para crear nueva cuenta */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Cuenta")
                }
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    CuentasList(cuentas)
                }
            }
        }
    }
}

@Composable
fun NavigationDrawer() {
    Column {
        Text("Menu", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        HorizontalDivider()
        // Agrega aquí los elementos del menú
        Text("Item 1", modifier = Modifier.padding(16.dp))
        Text("Item 2", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CuentasList(cuentas: List<Cuenta>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        cuentas.forEach { cuenta ->
            CuentaItem(cuenta)
            HorizontalDivider()
            /*Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text("${cuenta.nombre}: \$${cuenta.saldo}")
                }
            }*/
        }
    }
}

@Composable
fun CuentaItem(cuenta: Cuenta) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fechaFormateada = cuenta.fechaActualizacion.format(formatter)
    val formattedSaldo = formatCurrency(cuenta.saldo)

    ListItem(
        headlineContent = {
            Text(text = cuenta.nombre)
        },
        supportingContent = { Text(formattedSaldo) },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_balance_24),
                contentDescription = "Localized description",
            )
        },
        trailingContent = { Text(fechaFormateada) },
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

fun formatCurrency(amount: Double): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    return format.format(amount)
}