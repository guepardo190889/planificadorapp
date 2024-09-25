package com.example.planificadorapp.navegacion.drawer

import com.example.planificadorapp.R
import com.example.planificadorapp.navegacion.Ruta

enum class DrawerItem(
    val icono: Int,
    val titulo: String,
    val descripcion: String,
    val ruta: String
) {
    MOVIMIENTO(
        R.drawable.outline_swap_horiz_24,
        "Movimientos",
        "Movimientos",
        Ruta.MOVIMIENTOS.ruta
    ),
    CUENTAS(R.drawable.baseline_account_balance_24, "Cuentas", "Cuentas", Ruta.CUENTAS.ruta),
    PORTAFOLIOS(R.drawable.outline_work_24, "Portafolios", "Portafolios", Ruta.PORTAFOLIOS.ruta),
    ACTIVOS(R.drawable.outline_attach_money_24, "Activos", "Activos", Ruta.ACTIVOS.ruta),
    REPORTES(R.drawable.outline_bar_chart_24, "Reportes", "Reportes", Ruta.REPORTES.ruta),
    CONFIGURACION(
        R.drawable.outline_settings_24,
        "Configuración",
        "Configuración",
        Ruta.CONFIGURACIONES.ruta
    ),
}