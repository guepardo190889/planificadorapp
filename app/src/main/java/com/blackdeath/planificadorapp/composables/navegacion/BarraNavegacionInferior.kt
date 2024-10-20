package com.blackdeath.planificadorapp.composables.navegacion

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Composable que muestra la barra inferior de navegación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacionInferior(
    modifier: Modifier = Modifier,
    isTransaccionGuardar: Boolean? = true,
    onAtrasClick: (() -> Unit)? = null,
    onSiguienteClick: (() -> Unit)? = null,
    onTransaccionClick: (() -> Unit)? = null
) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBar(
            containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            if (onAtrasClick != null) {
                NavigationBarItem(icon = {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text("Atrás") } },
                        state = rememberTooltipState()
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Atrás"
                        )
                    }
                }, label = {
                    Text("Atrás")

                }, selected = false, onClick = { onAtrasClick() })
            }

            if (onSiguienteClick != null) {
                NavigationBarItem(icon = {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text("Adelante") } },
                        state = rememberTooltipState()
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = "Adelante"
                        )
                    }
                }, label = {

                    Text("Adelante")
                }, selected = false, onClick = { onSiguienteClick() })
            }

            if (onTransaccionClick != null) {
                NavigationBarItem(icon = {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text(if (isTransaccionGuardar == true) "Guardar" else "Actualizar") } },
                        state = rememberTooltipState()
                    ) {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = if (isTransaccionGuardar == true) "Guardar" else "Actualizar"
                        )
                    }
                }, label = {

                    Text(if (isTransaccionGuardar == true) "Guardar" else "Actualizar")
                }, selected = false, onClick = { onTransaccionClick() })
            }
        }
    }
}