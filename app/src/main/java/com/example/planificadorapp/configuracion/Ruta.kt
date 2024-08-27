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
    PORTAFOLIOS_GUARDAR("portafolios/guardar", "Guardar Portafolio", false),
    ACTIVOS("activos", "Activos", true),
    ACTIVOS_GUARDAR("activos/guardar", "Guardar Activo", false),
    ACTIVOS_DETALLE("detalle/{activoId}", "Detalle de Activo", false),
    REPORTES("reportes", "Reportes", true),
    CONFIGURACIONES("configuraciones", "Configuraciones", true);

    companion object {
        fun fromRuta(ruta: String): Ruta? {
            return entries.find { it.ruta == ruta }
        }
    }
}