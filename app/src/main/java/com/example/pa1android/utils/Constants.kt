package com.example.pa1android.utils

/**
 * URLs y constantes del proyecto, idénticas a ProyectoThor.
 */
const val API_URL = "https://jcmesia.alwaysdata.net/"
const val TERMINOS_ENDPOINT = "terminos.php"
const val PRODUCTOS_ENDPOINT = "productos.php"
const val IMAGENES_BASE_URL = "${API_URL}imagenes/"

/**
 * Función para armar la URL de la imagen, movida aquí para eliminar APIConfig
 * y seguir el estilo de centralización de Thor.
 */
fun getImagenURL(nombre: String?): String {
    val nombreLimpio = nombre?.trim() ?: ""
    return "$IMAGENES_BASE_URL$nombreLimpio.png"
}
