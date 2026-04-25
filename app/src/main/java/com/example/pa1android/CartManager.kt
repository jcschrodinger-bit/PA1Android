package com.example.pa1android

import androidx.compose.runtime.mutableStateMapOf

object CartManager { // creamos un Singleton: una sola instancia de memoria para toda la app
    val selectedProducts = mutableStateMapOf<String, Int>()// mutableStateMapOf permite que un cambio aquí actualice automáticamente cualquier pantalla

    fun toggleProduct(nombre: String, precio: Int) {// lógica en que si el producto ya existe lo elimina, de lo contrario, lo añade
        if (selectedProducts.containsKey(nombre)) {
            selectedProducts.remove(nombre)
        } else {
            selectedProducts[nombre] = precio
        }
    }

    fun addProduct(nombre: String, precio: Int) {
        selectedProducts[nombre] = precio // asegura la inserción directa desde la pantalla de detalles
    }

    fun isSelected(nombre: String): Boolean {
        return selectedProducts.containsKey(nombre) // consulta rápida para saber si pintar el "like" o no
    }

    fun getTotal(): Int {
        return selectedProducts.values.sum() // función de agregación para calcular el monto total de la compra en tiempo real
    }

    fun getProductsList(): List<Pair<String, Int>> {// convierte el mapa en una lista de pares para que sea fácil de iterar en una LazyColumn
        return selectedProducts.map { it.key to it.value }
    }
}