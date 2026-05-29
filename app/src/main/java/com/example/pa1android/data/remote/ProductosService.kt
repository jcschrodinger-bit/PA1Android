package com.example.pa1android.data.remote

import com.example.pa1android.models.Producto
import com.example.pa1android.utils.PRODUCTOS_ENDPOINT
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz de red idéntica a ProyectoThor.
 */
interface ProductosService {
    @GET(PRODUCTOS_ENDPOINT) // Usando la constante estilo Thor
    suspend fun getProductos(
        @Query("id_categoria") idcategoria: Int 
    ): List<Producto>

    @GET(PRODUCTOS_ENDPOINT) // Usando la constante estilo Thor
    suspend fun getProductoDetalle(
        @Query("id_producto") idproducto: Int
    ): List<Producto>
}
