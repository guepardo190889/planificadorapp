package com.example.planificadorapp.configuracion

enum class Ruta(
    val ruta: String,
    val titulo: String,
    val isPrincipal: Boolean = false
) {
    CUENTAS("cuentas", "Cuentas", true),
    DETALLE_CUENTA("detalle/{cuentaId}", "Detalle de Cuenta", false),
    MOVIMIENTOS("movimientos", "Movimientos", true),
    PORTAFOLIOS("portafolios", "Portafolios", true),
    REPORTES("reportes", "Reportes", true),
    CONFIGURACIONES("configuraciones", "Configuraciones", true);

    companion object {
        fun fromRuta(ruta: String): Ruta? {
            return entries.find { it.ruta == ruta }
        }
    }
}