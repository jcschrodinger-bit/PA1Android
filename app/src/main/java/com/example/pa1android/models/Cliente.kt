package com.example.pa1android.models

/**
 * Modelo de Cliente para manejar sesiones, idéntico a ProyectoThor.
 */
data class Cliente(
    val idcliente: Int,
    val nombre: String,
    val usuario: String,
    val token: String? = null
)
