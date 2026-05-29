package com.example.pa1android.pages.tienda

import com.example.pa1android.models.Categoria

/**
 * Estado de la pantalla Tienda (Categorías) siguiendo a Thor.
 */
sealed interface TiendaUIState {
    data object Loading : TiendaUIState
    data class Success(val Categorias: List<Categoria>) : TiendaUIState
    data class Error(val message: String): TiendaUIState
}
