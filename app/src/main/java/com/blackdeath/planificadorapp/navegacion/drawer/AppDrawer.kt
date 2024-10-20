package com.blackdeath.planificadorapp.navegacion.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blackdeath.planificadorapp.R

@Composable
fun AppDrawer(
    route: String,
    modifier: Modifier = Modifier,
    onClickMenuItem: (DrawerItem) -> Unit
) {
    val menus = DrawerItem.entries

    ModalDrawerSheet(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        DrawerHeader(modifier)
        Spacer(Modifier.padding(16.dp))

        menus.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icono),
                        contentDescription = item.descripcion,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(
                        text = item.titulo,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                selected = route == item.ruta,
                onClick = { onClickMenuItem(item) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer, // Color de fondo seleccionado
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,    // Texto cuando está seleccionado
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,    // Ícono cuando está seleccionado
                    unselectedContainerColor = MaterialTheme.colorScheme.background,     // Fondo no seleccionado
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,        // Texto no seleccionado
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground        // Ícono no seleccionado
                )
            )
        }
    }
}


@Composable
fun DrawerHeader(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Image(
            painterResource(id = R.drawable.outline_account_circle_24),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(24.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }
}