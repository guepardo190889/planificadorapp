package com.example.planificadorapp.screens.cuentas

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.R
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.repositorios.CuentaRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cuentas(navController: NavController, modifier: Modifier = Modifier) {
    val cuentaRepository = remember { CuentaRepository() }
    var cuentas by remember { mutableStateOf<List<CuentaModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        cuentaRepository.buscarCuentas { result ->
            Log.i("Cuentas", "Cuentas encontradas: $result")
            cuentas = result ?: emptyList()
            isLoading = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Guardar Cuenta")
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
            CuentasList(cuentas, navController, Modifier.padding(16.dp))

            if (showDialog) {
                GuardarCuentaDialog(
                    onDismiss = { showDialog = false },
                    onSave = { nuevaCuenta ->
                        cuentaRepository.guardarCuenta(nuevaCuenta) { cuentaGuardada ->
                            if (cuentaGuardada != null) {
                                cuentas = cuentas + cuentaGuardada

                                snackbarMessage = "Cuenta guardada exitosamenqte"
                                snackbarType = "success"

                                println("Cuenta guardada con ID: ${cuentaGuardada.id}")
                            } else {
                                snackbarMessage = "Error al guardar la cuenta"
                                snackbarType = "error"

                                println("Error al guardar la cuenta o fallo en la conexión")
                            }

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(snackbarMessage)
                            }
                        }

                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun CuentasList(
    cuentas: List<CuentaModel>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        cuentas.forEach { cuenta ->
            CuentaItem(cuenta, navController)
            HorizontalDivider()
        }
    }
}

@Composable
fun CuentaItem(cuenta: CuentaModel, navController: NavController) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fechaFormateada = cuenta.fechaActualizacion?.format(formatter)
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
        trailingContent = { Text(fechaFormateada ?: "") },
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("detalle/${cuenta.id}")
            }
    )
}

fun formatCurrency(amount: Double): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    return format.format(amount)
}

