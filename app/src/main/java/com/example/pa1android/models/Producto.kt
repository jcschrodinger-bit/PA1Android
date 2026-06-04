package com.example.pa1android.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos del producto sincronizado con ProyectoThor.
 * Mapea los nombres de la base de datos de AlwaysData a las variables usadas en clase.
 */
data class Producto(
    @SerializedName("id_producto") val idproducto: Int, 
    @SerializedName("id_categoria") val idcategoria: Int, 
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio_base") val precio: Double, 
    @SerializedName("imagen") val imagenchica: String?
) {
    // Propiedad para compatibilidad con lógica de descuentos de Thor
    val preciorebajado: Double = 0.0

    // Opciones de tallas fijas con inicialización segura
    @Transient
    val tallas: List<String> = listOf("36", "37", "38", "39", "40")
}
