package com.example.pa1android.Network

import com.example.pa1android.Producto
import com.example.pa1android.Terminos
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AlwaysDataApiService {
    @GET(APIConfig.TERMINOS_ENDPOINT)
    suspend fun obtenerTerminos(): Terminos

    @GET(APIConfig.PRODUCTOS_ENDPOINT)
    suspend fun obtenerProductos(): List<Producto>
}

object RetrofitClient {
    val apiService: AlwaysDataApiService by lazy {
        Retrofit.Builder()
            .baseUrl(APIConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlwaysDataApiService::class.java)
    }
}
