package com.example.pa1android.data.remote

import com.example.pa1android.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_URL) // Usando la constante estilo Thor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val productosService: ProductosService by lazy {
        retrofit.create(ProductosService::class.java)
    }

    val terminosService: TerminosService by lazy {
        retrofit.create(TerminosService::class.java)
    }

    val categoriasService: CategoriasService by lazy {
        retrofit.create(CategoriasService::class.java)
    }

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    val oficinasService: OficinasService by lazy {
        retrofit.create(OficinasService::class.java)
    }
}
