package com.example.pa1android.Network

object APIConfig {
    // URL principal del AlwaysData
    const val BASE_URL = "https://jcmesia.alwaysdata.net/"
    const val TERMINOS_ENDPOINT = "terminos.php"
    const val PRODUCTOS_ENDPOINT = "productos.php"

    // Ruta base para las imágenes en producción
    private const val IMAGENES_BASE_URL = "${BASE_URL}imagenes/"

    fun getImagenURL(nombre: String): String {
        val nombreLimpio = nombre.trim()
        return "$IMAGENES_BASE_URL$nombreLimpio.png"
    }
}