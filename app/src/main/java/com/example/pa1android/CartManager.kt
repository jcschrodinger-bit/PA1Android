package com.example.pa1android

import androidx.compose.runtime.mutableStateMapOf

object CartManager {
    // Mapa que almacena los productos seleccionados y sus precios
    // Usamos el nombre como clave y el precio como valor
    val selectedProducts = mutableStateMapOf<String, Int>()

    fun toggleProduct(nombre: String, precio: Int) {
        if (selectedProducts.containsKey(nombre)) {
            selectedProducts.remove(nombre)
        } else {
            selectedProducts[nombre] = precio
        }
    }

    fun addProduct(nombre: String, precio: Int) {
        selectedProducts[nombre] = precio
    }

    fun isSelected(nombre: String): Boolean {
        return selectedProducts.containsKey(nombre)
    }

    fun getTotal(): Int {
        return selectedProducts.values.sum()
    }
    
    fun getProductsList(): List<Pair<String, Int>> {
        return selectedProducts.map { it.key to it.value }
    }
}
