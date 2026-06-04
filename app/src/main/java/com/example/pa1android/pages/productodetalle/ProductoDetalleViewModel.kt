package com.example.pa1android.pages.productodetalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pa1android.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductoDetalleViewModel : ViewModel() {
    private val _uistate = MutableStateFlow<ProductoDetalleUIState>(ProductoDetalleUIState.Loading)
    val uiState: StateFlow<ProductoDetalleUIState> = _uistate.asStateFlow()

    fun fetchProductoDetalle(idproducto: Int) {
        viewModelScope.launch {
            _uistate.value = ProductoDetalleUIState.Loading
            try {
                val respuesta = RetrofitClient.productosService.getProductoDetalle(idproducto)
                
                // Buscamos el producto específico por su ID
                val productoEncontrado = respuesta.find { it.idproducto == idproducto } ?: respuesta.firstOrNull()
                
                if (productoEncontrado != null) {
                    _uistate.value = ProductoDetalleUIState.Success(productoEncontrado)
                } else {
                    _uistate.value = ProductoDetalleUIState.Error("Producto no encontrado")
                }
            } catch (e: Exception) {
                _uistate.value = ProductoDetalleUIState.Error("Error al cargar detalle: ${e.localizedMessage}")
            }
        }
    }
}
