package com.example.planificadorapp.composables.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

enum class DrawerItem (
    val icono:ImageVector,
    val titulo:String,
    val descripcion: String,
    val ruta:String
) {
    CUENTAS(Icons.Default.Info, "Cuentas", "Cuentas", "cuentas"),
    MOVIMIENTO(Icons.Default.Info, "Movimientos", "Movimientos", "movimientos"),
    PORTAFOLIOS(Icons.Default.Info, "Portafolios", "Portafolios", "portafolios"),
    REPORTES(Icons.Default.Info, "Reportes", "Reportes", "reportes"),
    CONFIGURACION(Icons.Default.Info, "Configuración", "Configuración", "configuraciones"),
}