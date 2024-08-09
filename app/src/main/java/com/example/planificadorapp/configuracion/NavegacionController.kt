package com.example.planificadorapp.configuracion

import android.view.MenuItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.planificadorapp.composables.BarraSuperior
import com.example.planificadorapp.composables.drawer.AppDrawer
import com.example.planificadorapp.composables.drawer.DrawerItem
import com.example.planificadorapp.screens.MovimientosScreen
import com.example.planificadorapp.screens.cuentas.Cuentas
import com.example.planificadorapp.screens.cuentas.DetalleCuentaScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavegacionController(modifier: Modifier = Modifier,
                         navController: NavHostController = rememberNavController(),
                         coroutineScope: CoroutineScope = rememberCoroutineScope(),
                         drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: DrawerItem.CUENTAS.ruta

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
            ) {
                NavHost(
                    navController = navController, startDestination = DrawerItem.CUENTAS.ruta, modifier = modifier.padding(it)
                ) {
                    composable(DrawerItem.CUENTAS.ruta) {
                        Cuentas(navController)
                    }
                    composable(DrawerItem.MOVIMIENTO.ruta) {
                        MovimientosScreen(navController)
                    }
                }
            }
        }
    )
//
//    NavHost(navController = navController, startDestination = "cuentas", modifier = Modifier) {
//        composable("cuentas") {
//            Cuentas(navController)
//        }
//        composable("detalle/{cuentaId}") { backStackEntry ->
//            val cuentaId = backStackEntry.arguments?.getString("cuentaId")
//            cuentaId?.let {
//                DetalleCuentaScreen(navController, it.toLong())
//            }
//        }
//    }
}