package com.example.pa1android.data.remote

import com.example.pa1android.models.Terminos
import com.example.pa1android.utils.TERMINOS_ENDPOINT
import retrofit2.http.GET

interface TerminosService {
    @GET(TERMINOS_ENDPOINT) // Usando la constante estilo Thor
    suspend fun obtenerTerminos(): Terminos
}
