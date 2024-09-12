package com.example.planificadorapp.composables.cuentas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.utilerias.FormatoMonto

/**
 * Composable que muestra la lista de cuentas
 */
@Composable
fun CuentasListSimple(
    modifier: Modifier = Modifier, cuentas: List<CuentaModel>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(cuentas) { cuenta ->
            CuentaItemSimple(modifier, cuenta)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        }
    }
}

/**
 * Composable que muestra un ítem de cuenta
 */
@Composable
fun CuentaItemSimple(
    modifier: Modifier, cuenta: CuentaModel
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(
                text = cuenta.nombre,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            Text(
                text = FormatoMonto.formato(cuenta.saldo),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}