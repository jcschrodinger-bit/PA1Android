package com.example.pa1android.models

import com.google.gson.annotations.SerializedName

data class Cliente(
    @SerializedName("id_cliente") val idcliente: Int,
    @SerializedName("nombre") val nombres: String?,
    @SerializedName("apellidos") val apellidos: String?,
    @SerializedName("email") val correotelefono: String?,
    @SerializedName("dni") val dni: Int?,
    @SerializedName("telefono") val telefono: String?
) {
    // Campos virtuales para compatibilidad con el resto de la App
    val empresa: String? = null
    val cargo: String? = null
    val ciudad: String? = "Lima"
    val pais: String? = "Perú"
}
