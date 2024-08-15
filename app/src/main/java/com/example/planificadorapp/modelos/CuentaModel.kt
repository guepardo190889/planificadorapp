package com.example.planificadorapp.modelos

import java.time.LocalDateTime

data class CuentaModel(
    var id: Long? = null,
    val nombre: String,
    val descripcion: String,
    val saldo: Double,
    var fechaActualizacion: LocalDateTime? = null
)

