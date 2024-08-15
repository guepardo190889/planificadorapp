package com.example.planificadorapp.screens.cuentas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.SnackBarConColor
import com.example.planificadorapp.modelos.CuentaModel
import com.example.planificadorapp.repositorios.CuentaRepository
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DetalleCuentaScreen(
    navController: NavController,
    cuentaId: Long,
    modifier: Modifier = Modifier
) {
    var cuenta by remember { mutableStateOf<CuentaModel?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val cuentaRepository = remember { CuentaRepository() }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarType by remember { mutableStateOf("") }

    LaunchedEffect(cuentaId) {
        cuentaRepository.buscarCuentaPorId(cuentaId) { result ->
            cuenta = result
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Actualizar Cuenta")
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
        cuenta?.let {
            Column(modifier = Modifier.padding(paddingValues)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = it.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = formatCurrency(it.saldo),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = it.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Última actualización: ${
                                it.fechaActualizacion?.format(
                                    DateTimeFormatter.ofPattern(
                                        "dd 'de' MMMM 'de' yyyy",
                                        Locale("es", "MX")
                                    )
                                )
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
                // Aquí puedes añadir los Sliders y Cards para Tareas y Comentarios
                /*Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tareas (2)",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Añade tu Slider aquí
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Comentarios (3)",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Añade tu Slider aquí
                    }
                }*/
            }
        }

        if (showDialog) {
            ActualizarCuentaDialog(
                cuenta = cuenta!!,
                onDismiss = { showDialog = false },
                onUpdate = { cuentaActualizada ->
                    cuentaRepository.actualizarCuenta(cuentaActualizada) { result ->
                        if (result != null) {
                            cuenta = result

                            snackbarMessage = "Cuenta guardada exitosamenqte"
                            snackbarType = "success"
                        } else {
                            snackbarMessage = "Error al guardar la cuenta"
                            snackbarType = "error"
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
