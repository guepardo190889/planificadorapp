package com.example.planificadorapp.screens.activos

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.example.planificadorapp.modelos.ActivoModel
import com.example.planificadorapp.repositorios.ActivosRepository

/**
 * Composable que representa la pantalla de activos
 */
@Composable
fun ActivosScreen(modifier: Modifier, navController: NavController) {
    val activosRepository = remember { ActivosRepository() }

    var activos by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        activosRepository.buscarActivos(false) { activosEncontrados ->
            activos = activosEncontrados ?: emptyList()
            Log.i("ActivosScreen", "Cargados ${activos.size} activos")
        }
    }

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("activos/guardar") },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Guardar Activo")
            }
        },
    ) { paddingValues ->
        Column(modifier.padding(paddingValues)) {
            ActivosList(activos, navController, Modifier.padding(16.dp))
        }
    }
}

/**
 * Composable que muestra la lista de activos
 */
@Composable
fun ActivosList(
    activos: List<ActivoModel>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier.padding(16.dp)) {
        items(activos) { activo ->
            ActivoItem(modifier, activo, navController)
            HorizontalDivider()
        }
    }
}

/**
 * Composable que muestra un ítem de activo
 */
@Composable
fun ActivoItem(modifier: Modifier, activo: ActivoModel, navController: NavController) {
    ListItem(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("activos/detalle/${activo.id}")
            },
        headlineContent = {
            Text(activo.nombre)
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_balance_24),
                contentDescription = "Localized description",
            )
        },
    )
}

