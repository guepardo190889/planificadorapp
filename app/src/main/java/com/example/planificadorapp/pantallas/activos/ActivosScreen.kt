package com.example.planificadorapp.pantallas.activos

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.R
import com.example.planificadorapp.composables.fab.FloatingActionButtonGuardar
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.navegacion.Ruta
import com.example.planificadorapp.repositorios.ActivosRepository

/**
 * Composable que representa la pantalla de activos
 */
@Composable
fun ActivosScreen(modifier: Modifier, navController: NavController) {
    val activosRepository = ActivosRepository()

    var activos by remember { mutableStateOf<List<ActivoModel>>(emptyList()) }

    val scrollState = rememberLazyListState()
    val isFabVisible by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }

    LaunchedEffect(Unit) {
        activosRepository.buscarActivos(false) { activosEncontrados ->
            activos = activosEncontrados ?: emptyList()
            Log.i("ActivosScreen", "Activos encontrados: ${activos.size}")
        }
    }

    Scaffold(modifier = modifier.fillMaxWidth(), floatingActionButton = {
        FloatingActionButtonGuardar(isVisible = isFabVisible,
            tooltip = "Guardar un nuevo activo",
            onClick = { navController.navigate(Ruta.ACTIVOS_GUARDAR.ruta) })
    }, content = { paddingValues ->
        Column(
            modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
                .padding(bottom = 56.dp)
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f), state = scrollState
            ) {
                itemsIndexed(activos) { indice, activo ->
                    ActivoItem(modifier, navController, activo)

                    // Añadir separador entre grupos de cuentas
                    val isUltimoElemento = indice == activos.size - 1
                    val isProximoPadre = !isUltimoElemento && activos[indice + 1].padre == null

                    if (isUltimoElemento || isProximoPadre) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            }
        }
    })
}

/**
 * Composable que muestra un ítem de activo
 */
@Composable
fun ActivoItem(
    modifier: Modifier,
    navController: NavController,
    activo: ActivoModel,
) {
    val paddingStart = if (activo.isHijo) 16.dp else 0.dp

    ListItem(modifier = modifier
        .padding(start = paddingStart)
        .clickable {
            navController.navigate("activos/detalle/${activo.id}")
        }, headlineContent = {
        Text(
            text = activo.nombre,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (activo.isHijo) FontWeight.Normal else FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }, supportingContent = {
        Text(
            text = if (activo.padre == null) "Activo" else "Subactivo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }, leadingContent = {
        Icon(
            painter = painterResource(id = R.drawable.outline_attach_money_24),
            contentDescription = "Icono de Activo",
            tint = MaterialTheme.colorScheme.primary
        )
    })
}