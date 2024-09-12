package com.example.planificadorapp.composables.cuentas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.planificadorapp.R
import com.example.planificadorapp.modelos.cuentas.CuentaModel
import com.example.planificadorapp.utilerias.FormatoMonto

/**
 * Composable que muestra la lista de cuentas
 */
@Composable
fun CuentasListConCheckbox(
    modifier: Modifier = Modifier,
    cuentas: List<CuentaModel>,
    onCuentaChequeada: (CuentaModel, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(cuentas) { cuenta ->
            CuentaItemConCheckbox(modifier, cuenta, onCuentaChequeada)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        }
    }
}

/**
 * Composable que muestra un ítem de cuenta
 */
@Composable
fun CuentaItemConCheckbox(
    modifier: Modifier,
    cuenta: CuentaModel,
    onCuentaChequeada: (CuentaModel, Boolean) -> Unit
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
        supportingContent = {
            Text(
                text = FormatoMonto.formato(cuenta.saldo),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_balance_24),
                contentDescription = "Icono de Cuenta",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            Checkbox(
                checked = cuenta.seleccionada,
                onCheckedChange = { isChecked ->
                    onCuentaChequeada(cuenta, isChecked)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    )
}