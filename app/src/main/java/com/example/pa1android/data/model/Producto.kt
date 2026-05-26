package com.example.pa1android.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Producto(
    @SerializedName("id_producto") val id: Int?,
    @SerializedName("id_categoria") val idCategoria: Int?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio_base") val precioBase: Double?,
    @SerializedName("imagen") val imagen: String?
) {
    // Opciones fijas integradas en el modelo
    val tallas: List<String> = listOf("36", "37", "38", "39", "40")

    /**
     * El equivalente exacto a 'idSeguro' de tu Swift.
     * Si por alguna razón el ID falla, genera un identificador seguro para evitar caídas en la grilla.
     */
    val idSeguro: String
        get() = id?.toString() ?: (nombre ?: UUID.randomUUID().toString())

    // Propiedad para compatibilidad con código que usa los nombres snake_case directamente
    val id_producto: String get() = id?.toString() ?: ""
    val id_categoria: String get() = idCategoria?.toString() ?: ""
    val precio_base: String get() = precioBase?.toString() ?: "0.0"
}
