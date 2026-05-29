package com.example.pa1android.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateMapOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartManager {
    val selectedProducts = mutableStateMapOf<String, Int>()
    private const val CART_KEY = "user_cart_data"
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("ElegancePrefs", Context.MODE_PRIVATE)
        loadCart()
    }

    fun addProduct(nombre: String?, precio: Int, talla: String, color: String) {
        val nombreSeguro = nombre ?: "Producto"
        val llaveUnica = "$nombreSeguro ($talla - $color)"
        selectedProducts[llaveUnica] = precio
        saveCart()
    }

    fun toggleProduct(nombre: String, precio: Int) {
        if (isSelected(nombre)) {
            selectedProducts.remove(nombre)
        } else {
            selectedProducts[nombre] = precio
        }
        saveCart()
    }

    fun isSelected(nombre: String): Boolean {
        return selectedProducts.containsKey(nombre)
    }

    fun getTotal(): Int {
        return selectedProducts.values.sum()
    }

    fun getItemsCount(): Int {
        return selectedProducts.size
    }

    fun clearCart() {
        selectedProducts.clear()
        saveCart()
    }

    fun getProductsList(): List<Pair<String, Int>> {
        return selectedProducts.map { it.key to it.value }.sortedBy { it.first }
    }

    private fun saveCart() {
        if (!::sharedPreferences.isInitialized) return
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
