package com.example.planificadorapp.modelos.cuentas

/**
 * Modelo de datos para guardar o actualizar una cuenta
 */
data class TransaccionCuentaRequestModel(
    var nombre: String,
    var descripcion: String,
    var saldo: Double = 0.0,
    var idPadre: Long? = null
)