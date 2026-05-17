package com.example.pa1android

import com.google.gson.annotations.SerializedName

data class Terminos(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("fecha_actualizacion") val fechaActualizacion: String,
    @SerializedName("seccion_1_titulo") val seccion1Titulo: String,
    @SerializedName("seccion_1_texto") val seccion1Texto: String,
    @SerializedName("seccion_2_titulo") val seccion2Titulo: String,
    @SerializedName("seccion_2_texto") val seccion2Texto: String,
    @SerializedName("seccion_3_titulo") val seccion3Titulo: String,
    @SerializedName("seccion_3_texto") val seccion3Texto: String
)
