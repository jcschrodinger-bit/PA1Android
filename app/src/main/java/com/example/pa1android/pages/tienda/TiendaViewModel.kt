package com.example.pa1android.pages.tienda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pa1android.R
import com.example.pa1android.models.Categoria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TiendaViewModel : ViewModel() {
    private val _uistate = MutableStateFlow<TiendaUIState>(TiendaUIState.Loading)
    val uiState: StateFlow<TiendaUIState> = _uistate.asStateFlow()

    fun fetchTienda() {
        viewModelScope.launch {
            _uistate.value = TiendaUIState.Loading
            try {
                // Simulación de carga de datos para Tienda, ya que las categorías son locales
                val lista = listOf(
                    Categoria("1", "Botas", R.drawable.img_zapato),
                    Categoria("2", "Zapatillas", R.drawable.img_portada),
                    Categoria("3", "Tacos", R.drawable.img_compra),
                    Categoria("4", "Sandalias", R.drawable.img_bienvenida)
                )
                _uistate.value = TiendaUIState.Success(lista)
            } catch (e: Exception) {
                _uistate.value = TiendaUIState.Error("Error al cargar categorías")
            }
        }
    }
}
