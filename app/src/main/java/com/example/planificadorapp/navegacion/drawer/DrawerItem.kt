package com.example.planificadorapp.navegacion.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.planificadorapp.navegacion.Ruta

enum class DrawerItem(
    val icono: ImageVector,
    val titulo: String,
    val descripcion: String,
    val ruta: String
) {
    CUENTAS(Icons.Default.Info, "Cuentas", "Cuentas", Ruta.CUENTAS.ruta),
    MOVIMIENTO(Icons.Default.Info, "Movimientos", "Movimientos", Ruta.MOVIMIENTOS.ruta),
    PORTAFOLIOS(Icons.Default.Info, "Portafolios", "Portafolios", Ruta.PORTAFOLIOS.ruta),
    ACTIVOS(Icons.Default.Info, "Activos", "Activos", Ruta.ACTIVOS.ruta),
    REPORTES(Icons.Default.Info, "Reportes", "Reportes", Ruta.REPORTES.ruta),
    CONFIGURACION(Icons.Default.Info, "Configuración", "Configuración", Ruta.CONFIGURACIONES.ruta),
}