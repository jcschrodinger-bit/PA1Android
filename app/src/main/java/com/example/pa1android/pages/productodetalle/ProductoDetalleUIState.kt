package com.example.pa1android.pages.productodetalle

import com.example.pa1android.models.Producto

/**
 * Estado de la pantalla Detalle siguiendo a Thor.
 */
sealed interface ProductoDetalleUIState {
    data object Loading : ProductoDetalleUIState
    data class Success(val producto: Producto) : ProductoDetalleUIState
    data class Error(val message: String): ProductoDetalleUIState
}
