package com.blackdeath.planificadorapp.pantallas.activos

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.blackdeath.planificadorapp.composables.TextoConEtiqueta
import com.blackdeath.planificadorapp.composables.fab.FloatingActionButtonActualizar
import com.blackdeath.planificadorapp.modelos.activos.ActivoModel
import com.blackdeath.planificadorapp.repositorios.ActivosRepository

/**
 * Composable que representa la pantalla de detalle de un activo
 */
@Composable
fun DetalleActivosScreen(modifier: Modifier, navController: NavController, idActivo: Long) {
    val activosRepository = ActivosRepository()

    var activo by remember { mutableStateOf<ActivoModel?>(null) }

    LaunchedEffect(idActivo) {
        activosRepository.buscarActivoPorId(idActivo) { activoEncontrado ->
            activo = activoEncontrado
            Log.i("DetalleActivosScreen", "Activo encontrado: $activo")
        }
    }

    Scaffold(modifier.fillMaxSize(), floatingActionButton = {
        if (activo != null) {
            FloatingActionButtonActualizar(isVisible = activo!!.isHijo,
                tooltip = "Actualizar el activo",
                onClick = { navController.navigate("activos/editar/${activo?.id}") })
        }
    }, content = { paddingValues ->
        Column(
            modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(bottom = 56.dp)
        ) {
            activo?.let { activo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(modifier.padding(16.dp)) {
                        if (activo.isPadre) {
                            AssistChip(
                                onClick = { },
                                label = { Text("Principal") },
                                colors = AssistChipDefaults.assistChipColors(),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        if (activo.isHijo) {
                            TextoConEtiqueta(
                                "Activo principal: ", activo.padre!!.nombre, "large", "medium"
                            )
                        }

                        TextoConEtiqueta("Nombre: ", activo.nombre, "large", "medium")
                        TextoConEtiqueta(
                            "Descripción: ", activo.descripcion ?: "", "large", "medium"
                        )
                    }
                }
            }
        }
    })
}