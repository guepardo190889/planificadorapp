package com.example.planificadorapp

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.planificadorapp.componentes.ConfiguracionScreen
import com.example.planificadorapp.componentes.cuentas.Cuentas
import com.example.planificadorapp.componentes.MovimientosScreen
import com.example.planificadorapp.componentes.PortafoliosScreen
import com.example.planificadorapp.componentes.ReportesScreen
import com.example.planificadorapp.modelos.Menu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val elementosMenu = listOf(
        Menu(R.drawable.baseline_account_balance_24, "Cuentas"),
        Menu(R.drawable.baseline_account_balance_24, "Movimientos"),
        Menu(R.drawable.baseline_account_balance_24, "Portafolios"),
        Menu(R.drawable.baseline_account_balance_24, "Reportes"),
        Menu(R.drawable.baseline_account_balance_24, "Configuración")
    )

    var menuSeleccionado by remember { mutableStateOf(elementosMenu[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuComposable(
                elementosMenu = elementosMenu,
                menuSeleccionado = menuSeleccionado,
                onMenuSeleccionado = { item ->
                    menuSeleccionado = item
                    scope.launch { drawerState.close() }
                }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(menuSeleccionado.titulo) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Log.i("Principal", "Menu seleccionado (when): ${menuSeleccionado.titulo}")
                when (menuSeleccionado.titulo) {
                    "Cuentas" -> Cuentas(navController, modifier = Modifier.padding(paddingValues))
                    "Movimientos" -> MovimientosScreen(modifier = Modifier.padding(paddingValues))
                    "Portafolios" -> PortafoliosScreen(modifier = Modifier.padding(paddingValues))
                    "Reportes" -> ReportesScreen(modifier = Modifier.padding(paddingValues))
                    "Configuración" -> ConfiguracionScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    )
}

