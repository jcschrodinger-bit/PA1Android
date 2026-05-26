package com.example.pa1android.data.network

import com.example.pa1android.data.model.Producto
import com.example.pa1android.data.model.Terminos
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AlwaysDataApiService {
    @GET(APIConfig.TERMINOS_ENDPOINT)
    suspend fun obtenerTerminos(): Terminos

    @GET(APIConfig.PRODUCTOS_ENDPOINT)
    suspend fun obtenerProductos(@Query("id_categoria") idCategoria: String): List<Producto>
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
