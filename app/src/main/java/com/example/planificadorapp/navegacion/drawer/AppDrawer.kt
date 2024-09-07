package com.example.planificadorapp.navegacion.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
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
import com.example.planificadorapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    route: String,
    modifier: Modifier = Modifier,
    onClickMenuItem: (DrawerItem) -> Unit
) {
    val menus = DrawerItem.entries

    ModalDrawerSheet(modifier = Modifier) {
        DrawerHeader(modifier)
        Spacer(Modifier.padding(16.dp))

        menus.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.icono, contentDescription = item.descripcion) },
                label = { Text(item.titulo) },
                selected = route == item.ruta,
                onClick = { onClickMenuItem(item) }
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
            painterResource(id = R.drawable.baseline_account_balance_24),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(16.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}