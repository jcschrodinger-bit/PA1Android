package com.example.pa1android.utils

const val API_URL = "http://jcmesia.alwaysdata.net/"
const val TERMINOS_ENDPOINT = "terminos.php"
const val PRODUCTOS_ENDPOINT = "productos.php"
const val IMAGENES_BASE_URL = "${API_URL}imagenes/"

fun getImagenURL(nombre: String?): String {
    if (nombre.isNullOrBlank()) return ""
    val nombreLimpio = nombre.trim()
    
    if (nombreLimpio.startsWith("http")) return nombreLimpio
    
    return if (nombreLimpio.contains(".")) {
        "$IMAGENES_BASE_URL$nombreLimpio"
    } else {
        "$IMAGENES_BASE_URL$nombreLimpio.png"
    }
}
