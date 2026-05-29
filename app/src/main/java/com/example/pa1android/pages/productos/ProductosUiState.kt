package com.example.pa1android.pages.productos

import com.example.pa1android.models.Producto

/**
 * Interfaz de estado sellada idéntica a ProyectoThor.
 */
sealed interface ProductosUIState {
    data object Loading : ProductosUIState
    data class Success(val Productos: List<Producto>) : ProductosUIState
    data class Error(val message: String): ProductosUIState
}
