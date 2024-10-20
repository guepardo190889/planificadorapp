package com.blackdeath.planificadorapp.composables.cuentas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.blackdeath.planificadorapp.utilerias.FormatoMonto
import com.blackdeath.planificadorapp.modelos.cuentas.CuentaModel

/**
 * Composable que muestra la lista de cuentas
 */
@Composable
fun CuentasListSimple(
    modifier: Modifier = Modifier, cuentas: List<CuentaModel>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        cuentas.forEach { cuenta ->
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