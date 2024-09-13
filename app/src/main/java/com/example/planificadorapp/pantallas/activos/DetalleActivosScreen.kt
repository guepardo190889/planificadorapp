package com.example.planificadorapp.pantallas.activos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.planificadorapp.composables.TextoConEtiqueta
import com.example.planificadorapp.modelos.activos.ActivoModel
import com.example.planificadorapp.repositorios.ActivosRepository

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
        }
    }

    Scaffold(
        modifier.fillMaxSize(),
        floatingActionButton = {
            if (activo != null && activo!!.padre != null) {
                FloatingActionButton(
                    modifier = Modifier,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = { navController.navigate("activos/editar/${activo?.id}") },
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Actualizar Activo")
                }
            }
        },
        content = { paddingValues ->
            activo?.let {
                Column(modifier.padding(paddingValues)) {
                    Card(
                        modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Column(modifier.padding(16.dp)) {
                            TextoConEtiqueta("Activo principal: ", it.nombre, "large", "medium")
                            TextoConEtiqueta("Nombre: ", it.nombre, "large", "medium")
                            TextoConEtiqueta("Descripción: ", it.descripcion, "large", "medium")
                        }
                    }
                }
            }
        }
    )
}