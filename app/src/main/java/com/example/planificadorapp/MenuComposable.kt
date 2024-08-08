package com.example.planificadorapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.modelos.Menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuComposable(
    elementosMenu: List<Menu>,
    menuSeleccionado: Menu,
    onMenuSeleccionado: (Menu) -> Unit
) {
    ModalDrawerSheet {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(12.dp))
            elementosMenu.forEach { item ->
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icono),
                            contentDescription = ""
                        )
                    },
                    label = { Text(item.titulo) },
                    selected = item == menuSeleccionado,
                    onClick = {
                        onMenuSeleccionado(item)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }
}