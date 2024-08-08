package com.example.planificadorapp.configuracion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planificadorapp.componentes.cuentas.Cuentas
import com.example.planificadorapp.componentes.cuentas.DetalleCuentaComposable
import com.example.planificadorapp.modelos.Cuenta
import com.google.gson.Gson

@Composable
fun NavegacionController() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "cuentas") {
        composable("cuentas") {
            Cuentas(navController)
        }
        composable("detalle/{cuentaId}") { backStackEntry ->
            val cuentaId = backStackEntry.arguments?.getString("cuentaId")
            cuentaId?.let {
                DetalleCuentaComposable(it.toLong())
            }
        }
    }
}