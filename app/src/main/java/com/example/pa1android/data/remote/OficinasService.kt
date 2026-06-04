package com.example.pa1android.data.remote

import com.example.pa1android.models.Oficina
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface OficinasService {
    @GET("oficinas.php")
    suspend fun getOficinas(): List<Oficina>

    @FormUrlEncoded
    @POST("oficinasinsert.php")
    suspend fun insertOficina(
        @Field("nombre") nombre: String,
        @Field("ciudad") ciudad: String
    ): String

    @FormUrlEncoded
    @POST("oficinasupdate.php")
    suspend fun updateOficina(
        @Field("idoficina") idoficina: String,
        @Field("nombre") nombre: String,
        @Field("ciudad") ciudad: String
    ): String
}
