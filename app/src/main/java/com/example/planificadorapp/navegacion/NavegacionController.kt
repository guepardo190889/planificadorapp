package com.example.planificadorapp.navegacion

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.planificadorapp.composables.BarraSuperior
import com.example.planificadorapp.navegacion.drawer.AppDrawer
import com.example.planificadorapp.navegacion.drawer.DrawerItem
import com.example.planificadorapp.pantallas.ConfiguracionScreen
import com.example.planificadorapp.pantallas.ReportesScreen
import com.example.planificadorapp.pantallas.activos.ActivosScreen
import com.example.planificadorapp.pantallas.activos.DetalleActivosScreen
import com.example.planificadorapp.pantallas.activos.TransaccionActivosScreen
import com.example.planificadorapp.pantallas.cuentas.ActualizarCuentasScreen
import com.example.planificadorapp.pantallas.cuentas.Cuentas
import com.example.planificadorapp.pantallas.cuentas.DetalleCuentasScreen
import com.example.planificadorapp.pantallas.cuentas.GuardarCuentasScreen
import com.example.planificadorapp.pantallas.movimientos.DetalleMovimientosScreen
import com.example.planificadorapp.pantallas.movimientos.MovimientosScreen
import com.example.planificadorapp.pantallas.movimientos.TransaccionMovimientosScreen
import com.example.planificadorapp.pantallas.portafolios.DetallePortafoliosScreen
import com.example.planificadorapp.pantallas.portafolios.Portafolios
import com.example.planificadorapp.pantallas.portafolios.actualizado.ActualizarPortafolio
import com.example.planificadorapp.pantallas.portafolios.guardado.GuardarPortafolio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Componente que se encarga de administrar la navegación.
 *
 * Este componente sabe cuál es la primera pantalla que se debe mostrar
 */
@Composable
fun NavegacionController(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: Ruta.CUENTAS.ruta
    var drawerItemSeleccionado by remember { mutableStateOf<DrawerItem?>(DrawerItem.CUENTAS) }
    var tituloBarraSuperior by remember { mutableStateOf("Planificador") }
    var isPantallaPrincipal by remember { mutableStateOf(true) }

    LaunchedEffect(currentRoute) {
        Log.i("NavegacionController", "Ruta actual: $currentRoute")
        val ruta = Ruta.fromRuta(currentRoute)
        isPantallaPrincipal = ruta?.isPrincipal ?: false
        tituloBarraSuperior = ruta?.titulo ?: "Planificador"
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        AppDrawer(route = currentRoute, modifier = Modifier, onClickMenuItem = {
            Log.i("NavegacionController", "Item seleccionado: $it")
            drawerItemSeleccionado = it
            navController.navigate(it.ruta) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
            coroutineScope.launch { drawerState.close() }
        })
    }, content = {
        Scaffold(
            topBar = {
                BarraSuperior(titulo = tituloBarraSuperior,
                    isPantallaPrincipal = isPantallaPrincipal,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onBarraSuperiorIconClick = {
                        if (isPantallaPrincipal) {
                            coroutineScope.launch { drawerState.open() }
                        } else {
                            navController.popBackStack()
                        }
                    })
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) { it ->
            NavHost(
                navController = navController,
                startDestination = Ruta.CUENTAS.ruta,
                modifier = modifier.padding(it)
            ) {
                composable(Ruta.CUENTAS.ruta) {
                    Cuentas(modifier, navController)
                }
                composable(Ruta.CUENTAS_GUARDAR.ruta) {
                    GuardarCuentasScreen(modifier, navController)
                }
                composable(Ruta.CUENTAS_DETALLE.ruta) {
                    val idCuenta = it.arguments?.getString("idCuenta")

                    if (idCuenta != null && idCuenta.toLong() > 0) {
                        DetalleCuentasScreen(modifier, navController, idCuenta.toLong())
                    }
                }
                composable(Ruta.CUENTAS_EDITAR.ruta) {
                    val idCuenta = it.arguments?.getString("idCuenta")

                    if (idCuenta != null && idCuenta.toLong() > 0) {
                        ActualizarCuentasScreen(modifier, navController, idCuenta.toLong())
                    }
                }

                composable(Ruta.MOVIMIENTOS.ruta) {
                    MovimientosScreen(modifier, navController)
                }
                composable(Ruta.MOVIMIENTOS_GUARDAR.ruta) {
                    TransaccionMovimientosScreen(modifier, navController, 0L)
                }
                composable(Ruta.MOVIMIENTOS_DETALLE.ruta) {
                    val idMovimiento = it.arguments?.getString("idMovimiento")

                    if (idMovimiento != null && idMovimiento.toLong() > 0) {
                        DetalleMovimientosScreen(modifier, navController, idMovimiento.toLong())
                    }
                }
                composable(Ruta.MOVIMIENTOS_EDITAR.ruta) {
                    val idMovimiento = it.arguments?.getString("idMovimiento")

                    if (idMovimiento != null && idMovimiento.toLong() > 0) {
                        TransaccionMovimientosScreen(
                            modifier, navController, idMovimiento.toLong()
                        )
                    }
                }

                composable(Ruta.PORTAFOLIOS.ruta) {
                    Portafolios(modifier, navController)
                }
                composable(Ruta.PORTAFOLIOS_GUARDAR.ruta) {
                    GuardarPortafolio(modifier, navController)
                }
                composable(Ruta.PORTAFOLIOS_DETALLE.ruta) {
                    val idPortafolio = it.arguments?.getString("idPortafolio")

                    if (idPortafolio != null && idPortafolio.toLong() > 0) {
                        DetallePortafoliosScreen(modifier, navController, idPortafolio.toLong())
                    }
                }
                composable(Ruta.PORTAFOLIOS_EDITAR.ruta) {
                    val idPortafolio = it.arguments?.getString("idPortafolio")

                    if (idPortafolio != null && idPortafolio.toLong() > 0) {
                        ActualizarPortafolio(modifier, navController, idPortafolio.toLong())
                    }
                }

                composable(Ruta.ACTIVOS.ruta) {
                    ActivosScreen(modifier, navController)
                }
                composable(Ruta.ACTIVOS_GUARDAR.ruta) {
                    TransaccionActivosScreen(modifier, navController, 0L)
                }
                composable(Ruta.ACTIVOS_DETALLE.ruta) {
                    val idActivo = it.arguments?.getString("idActivo")

                    if (idActivo != null && idActivo.toLong() > 0) {
                        DetalleActivosScreen(modifier, navController, idActivo.toLong())
                    }
                }
                composable(Ruta.ACTIVOS_EDITAR.ruta) {
                    val idActivo = it.arguments?.getString("idActivo")

                    if (idActivo != null && idActivo.toLong() > 0) {
                        TransaccionActivosScreen(modifier, navController, idActivo.toLong())
                    }
                }

                composable(Ruta.REPORTES.ruta) {
                    ReportesScreen(navController)
                }

                composable(Ruta.CONFIGURACIONES.ruta) {
                    ConfiguracionScreen(navController)
                }
            }
        }
    })
}