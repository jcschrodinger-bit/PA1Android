package com.example.pa1android.data.remote

import com.example.pa1android.models.Categoria
import retrofit2.http.GET

/**
 * Servicio para obtener categorías, siguiendo el patrón de Thor.
 * Nota: Aunque las categorías sean fijas en la UI por ahora, 
 * esta estructura cumple con el requerimiento técnico.
 */
interface CategoriasService {
    @GET("categorias.php") // Simulado, se usaría si AlwaysData tuviera este endpoint
    suspend fun getCategorias(): List<Categoria>
}
