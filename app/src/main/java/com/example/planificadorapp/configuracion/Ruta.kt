package com.example.planificadorapp.configuracion

enum class Ruta(
    val ruta: String
) {
    CUENTAS("cuentas"),
    DETALLE_CUENTA("detalle/{cuentaId}"),
    MOVIMIENTOS("movimientos"),
    PORTAFOLIOS("portafolios"),
    REPORTES("reportes"),
    CONFIGURACIONES("configuraciones")
}