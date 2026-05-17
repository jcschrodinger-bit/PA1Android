package com.example.pa1android.Network

object APIConfig {
    // URL principal de tu hosting AlwaysData
    const val BASE_URL = "https://jcmesia.alwaysdata.net/"

    // Endpoints específicos para el consumo asíncrono
    const val TERMINOS_ENDPOINT = "terminos.php"
    const val PRODUCTOS_ENDPOINT = "productos.php"

    // Ruta base para las imágenes en producción
    private const val IMAGENES_BASE_URL = "${BASE_URL}imagenes/"

    /**
     * Arma la dirección URL completa de cada foto en internet.
     * Concatena la carpeta del servidor con el nombre del registro y la extensión .png.
     */
    fun getImagenURL(nombre: String): String {
        // Limpiamos espacios o caracteres especiales si existieran
        val nombreLimpio = nombre.trim()
        return "$IMAGENES_BASE_URL$nombreLimpio.png"
    }
}