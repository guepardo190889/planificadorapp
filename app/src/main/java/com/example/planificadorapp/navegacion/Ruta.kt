package com.example.planificadorapp.navegacion

enum class Ruta(
    val ruta: String,
    val titulo: String,
    val isPrincipal: Boolean = false
) {
    CUENTAS("cuentas", "Cuentas", true),
    CUENTAS_GUARDAR("cuentas/guardar", "Guardar Cuenta"),
    CUENTAS_DETALLE("cuentas/detalle/{idCuenta}", "Detalle de Cuenta", false),
    CUENTAS_EDITAR("cuentas/editar/{idCuenta}", "Editar Cuenta", false),

    MOVIMIENTOS("movimientos", "Movimientos", true),
    MOVIMIENTOS_GUARDAR("movimientos/guardar", "Guardar Movimiento", false),
    MOVIMIENTOS_DETALLE("movimientos/detalle/{idMovimiento}", "Detalle de Movimiento", false),
    MOVIMIENTOS_EDITAR("movimientos/editar/{idMovimiento}", "Editar Movimiento", false),

    PORTAFOLIOS("portafolios", "Portafolios", true),
    PORTAFOLIOS_GUARDAR("portafolios/guardar", "Guardar Portafolio", false),
    PORTAFOLIOS_DETALLE("portafolios/detalle/{idPortafolio}", "Detalle de Portafolio", false),

    ACTIVOS("activos", "Activos", true),
    ACTIVOS_GUARDAR("activos/guardar", "Guardar Activo", false),
    ACTIVOS_DETALLE("activos/detalle/{idActivo}", "Detalle de Activo", false),
    ACTIVOS_EDITAR("activos/editar/{idActivo}", "Editar Activo", false),

    REPORTES("reportes", "Reportes", true),

    CONFIGURACIONES("configuraciones", "Configuraciones", true);

    companion object {
        fun fromRuta(ruta: String): Ruta? {
            return entries.find { it.ruta == ruta }
        }
    }
}