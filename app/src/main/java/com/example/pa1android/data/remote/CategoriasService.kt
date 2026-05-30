package com.example.pa1android.data.remote

import com.example.pa1android.models.Categoria
import retrofit2.http.GET

interface CategoriasService {
    @GET("categorias.php") // Simulado, se usaría si AlwaysData tuviera este endpoint
    suspend fun getCategorias(): List<Categoria>
}
