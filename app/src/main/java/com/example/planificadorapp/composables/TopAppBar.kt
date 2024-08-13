package com.example.planificadorapp.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    isPantallaPrincipal: Boolean = true,
    onBarraSuperiorIconClick: () -> Unit
) {
    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            IconButton(onClick = { onBarraSuperiorIconClick() }) {
                if (isPantallaPrincipal) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                } else {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Atrás")
                }
            }
        }
    )
}