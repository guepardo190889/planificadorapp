package com.example.planificadorapp.configuracion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.planificadorapp.composables.BarraSuperior
import com.example.planificadorapp.composables.drawer.AppDrawer
import com.example.planificadorapp.composables.drawer.DrawerItem
import com.example.planificadorapp.screens.ConfiguracionScreen
import com.example.planificadorapp.screens.MovimientosScreen
import com.example.planificadorapp.screens.PortafoliosScreen
import com.example.planificadorapp.screens.ReportesScreen
import com.example.planificadorapp.screens.cuentas.Cuentas
import com.example.planificadorapp.screens.cuentas.DetalleCuentaScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavegacionController(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: Ruta.CUENTAS.ruta

    var drawerItemSeleccionado: DrawerItem? = null

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(route = currentRoute,
                modifier = Modifier,
                onClickMenuItem = {
                    drawerItemSeleccionado = it;
                    navController.navigate(it.ruta)
                    coroutineScope.launch { drawerState.close() }
                })
        },
        content = {
            Scaffold(
                topBar = {
                    BarraSuperior(
                        titulo = drawerItemSeleccionado?.titulo ?: "Planificador",
                        onBarraSuperiorClick = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }
            ) { it ->
                NavHost(
                    navController = navController,
                    startDestination = Ruta.CUENTAS.ruta,
                    modifier = modifier.padding(it)
                ) {
                    composable(Ruta.CUENTAS.ruta) {
                        Cuentas(navController)
                    }
                    composable(Ruta.DETALLE_CUENTA.ruta) {
                        val cuentaId = it.arguments?.getString("cuentaId")

                        if (cuentaId != null) {
                            DetalleCuentaScreen(navController, cuentaId.toLong())
                        }
                    }
                    composable(Ruta.MOVIMIENTOS.ruta) {
                        MovimientosScreen(navController)
                    }
                    composable(Ruta.PORTAFOLIOS.ruta) {
                        PortafoliosScreen(navController)
                    }
                    composable(Ruta.REPORTES.ruta) {
                        ReportesScreen(navController)
                    }
                    composable(Ruta.CONFIGURACIONES.ruta) {
                        ConfiguracionScreen(navController)
                    }
                }
            }
        }
    )
}