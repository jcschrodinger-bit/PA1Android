package com.example.pa1android.data.remote

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("login.php")
    suspend fun getLogin(
        @Field("correotelefono") correotelefono: String,
        @Field("clave") clave: String
    ): String
}
