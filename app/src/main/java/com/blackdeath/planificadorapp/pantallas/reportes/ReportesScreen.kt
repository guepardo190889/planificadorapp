package com.blackdeath.planificadorapp.pantallas.reportes

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.blackdeath.planificadorapp.utilerias.Cadena
import com.blackdeath.planificadorapp.utilerias.enumeradores.Reporte
import com.blackdeath.planificadorapp.composables.snackbar.SnackBarBase
import com.blackdeath.planificadorapp.composables.snackbar.SnackBarManager
import com.blackdeath.planificadorapp.composables.snackbar.SnackBarTipo
import com.blackdeath.planificadorapp.modelos.reportes.ReporteMenuResponseModel
import com.blackdeath.planificadorapp.repositorios.ReportesRepository

/**
 * Composable que representa la pantalla de reportes
 */
@Composable
fun ReportesScreen(modifier: Modifier, navController: NavController) {
    val reportesRepository = ReportesRepository()

    var menuReportes by remember { mutableStateOf<List<ReporteMenuResponseModel>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarManager = remember { SnackBarManager(coroutineScope, snackbarHostState) }

    LaunchedEffect(Unit) {
        reportesRepository.buscarReportes { reportesEncontrados ->
            menuReportes = reportesEncontrados ?: emptyList()
            Log.i("ReportesScreen", "Menús reportes encontrados: ${menuReportes.size}")
        }
    }

    Scaffold(modifier = modifier.fillMaxWidth(),
    snackbarHost = {
        SnackBarBase(
            snackbarHostState = snackbarHostState, snackBarManager = snackBarManager
        )
    },
        content = { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
                .padding(bottom = 56.dp)
        ) {
            items(menuReportes) { menuReporte ->
                ReporteMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    reporteMenuResponseModel = menuReporte,
                    snackBarManager = snackBarManager,
                    navController = navController
                )
            }
        }
    })
}

/**
 * Composable que representa un elemento del menú de reportes
 */
@Composable
fun ReporteMenuItem(
    modifier: Modifier, reporteMenuResponseModel: ReporteMenuResponseModel, snackBarManager: SnackBarManager, navController: NavController
) {
    var isMostrarReportes by remember { mutableStateOf(false) }

    Column {
        ListItem(modifier = modifier
            .fillMaxWidth()
            .clickable {
                isMostrarReportes = !isMostrarReportes
            }, headlineContent = {
            Text(
                text = Cadena.capitalizarPrimeraLetra(reporteMenuResponseModel.categoria.name),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }, trailingContent = {
            Icon(
                imageVector = if (isMostrarReportes) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isMostrarReportes) "Contraer" else "Expandir",
                tint = MaterialTheme.colorScheme.primary
            )
        })

        if (isMostrarReportes) {
            if (reporteMenuResponseModel.reportes.isNotEmpty()) {
                reporteMenuResponseModel.reportes.forEach { reporte ->
                    ListItem(modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .clickable {
                            val reporteEnum = Reporte.obtenerPorId(reporte.id)
                            Log.i("ReportesScreen", "Reporte seleccionado: $reporte")

                            if (reporteEnum != null) {
                                navController.navigate(reporteEnum.ruta.ruta)
                                Log.i("ReportesScreen", "Visualizar reporte: ${reporte.nombre}")
                            } else {
                                snackBarManager.mostrar("Reporte no disponible", SnackBarTipo.ERROR)
                                Log.i("ReportesScreen", "Reporte no reconocido: ${reporte.nombre}")
                            }
                        }, headlineContent = {
                        Text(
                            text = reporte.nombre,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    })
                }
            } else {
                Text(
                    modifier = modifier.padding(start = 16.dp),
                    text = "Sin reportes aún",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.outline
        )
    }
}