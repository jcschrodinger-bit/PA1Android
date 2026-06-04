package com.example.pa1android.utils

/**
 * URLs y constantes del proyecto, idénticas a ProyectoThor.
 */
const val API_URL = "http://jcmesia.alwaysdata.net/"
const val TERMINOS_ENDPOINT = "terminos.php"
const val PRODUCTOS_ENDPOINT = "productos.php"
const val IMAGENES_BASE_URL = "${API_URL}imagenes/"

/**
 * Función para armar la URL de la imagen, movida aquí para eliminar APIConfig
 * y seguir el estilo de centralización de Thor.
 */
fun getImagenURL(nombre: String?): String {
    if (nombre.isNullOrBlank()) return ""
    val nombreLimpio = nombre.trim()
    
    // Si el servidor ya manda la URL completa, la usamos
    if (nombreLimpio.startsWith("http")) return nombreLimpio
    
    // Si ya tiene extensión, no agregamos .png
    return if (nombreLimpio.contains(".")) {
        "$IMAGENES_BASE_URL$nombreLimpio"
    } else {
        "$IMAGENES_BASE_URL$nombreLimpio.png"
    }
}
