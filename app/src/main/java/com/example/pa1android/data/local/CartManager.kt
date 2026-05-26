package com.example.pa1android.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateMapOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartManager { // Instancia única (Singleton), equivalente a static let shared en Swift

    // Almacén de estado reactivo para Compose, mapea "Llave Única" -> Precio
    val selectedProducts = mutableStateMapOf<String, Int>()

    private const val CART_KEY = "user_cart_data"
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    /**
     * Inicializador obligatorio para Android. Se debe invocar en el onCreate de la MainActivity
     * o la Application class para levantar los datos guardados en el disco del celular.
     */
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("ElegancePrefs", Context.MODE_PRIVATE)
        loadCart()
    }

    /**
     * Inserta un producto directo desde la pantalla de detalles usando una
     * llave compuesta idéntica a la lógica implementada en Xcode.
     */
    fun addProduct(nombre: String?, precio: Int, talla: String, color: String) {
        val nombreSeguro = nombre ?: "Producto"
        val llaveUnica = "$nombreSeguro ($talla - $color)" // Llave espejo compuesta
        selectedProducts[llaveUnica] = precio
        saveCart()
    }

    /**
     * Agrega o elimina un producto de forma directa mediante un toque (utilizado en el listado maestro).
     */
    fun toggleProduct(nombre: String, precio: Int) {
        if (isSelected(nombre)) { // Si ya está, lo quitamos
            selectedProducts.remove(nombre)
        } else { // Si no está, lo agregamos
            selectedProducts[nombre] = precio
        }
        saveCart()
    }

    /**
     * Consulta rápida para saber si el calzado ya se encuentra en la lista de compras.
     */
    fun isSelected(nombre: String): Boolean {
        return selectedProducts.containsKey(nombre)
    }

    /**
     * Equivalente a totalPrice en Swift: reduce todos los valores sumándolos en tiempo real.
     */
    fun getTotal(): Int {
        return selectedProducts.values.sum()
    }

    /**
     * Equivalente a itemsCount en Swift.
     */
    fun getItemsCount(): Int {
        return selectedProducts.size
    }

    /**
     * Limpia por completo el carrito (Equivalente a clearCart).
     */
    fun clearCart() {
        selectedProducts.clear()
        saveCart()
    }

    /**
     * Convierte el mapa a una lista ordenada alfabéticamente para renderizar en la LazyColumn de Compras.
     * Replica el comportamiento del sortedProductNames en Xcode.
     */
    fun getProductsList(): List<Pair<String, Int>> {
        return selectedProducts.map { it.key to it.value }.sortedBy { it.first }
    }

    // ==========================================
    // PERSISTENCIA DE DATOS (SharedPreferences)
    // ==========================================

    private fun saveCart() {
        if (!::sharedPreferences.isInitialized) return

        // Convertimos el mapa de Compose a un mapa estándar de Java para que Gson lo pueda procesar
        val standardMap = selectedProducts.toMap()
        val jsonString = gson.toJson(standardMap)

        sharedPreferences.edit().putString(CART_KEY, jsonString).apply()
    }

    private fun loadCart() {
        if (!::sharedPreferences.isInitialized) return

        val jsonString = sharedPreferences.getString(CART_KEY, null)
        if (jsonString != null) {
            try {
                val type = object : TypeToken<Map<String, Int>>() {}.type
                val decodedMap: Map<String, Int> = gson.fromJson(jsonString, type)

                selectedProducts.clear()
                selectedProducts.putAll(decodedMap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}