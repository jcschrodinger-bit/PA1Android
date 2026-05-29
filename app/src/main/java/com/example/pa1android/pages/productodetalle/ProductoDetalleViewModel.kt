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
                // CORRECCIÓN DE LÓGICA: 
                // Si el servidor devuelve una lista completa o no filtra bien por ID,
                // buscamos el producto específico dentro de la respuesta para asegurar que no sea siempre el primero.
                val respuesta = RetrofitClient.productosService.getProductoDetalle(idproducto)
                
                // Buscamos el producto que coincida con el ID solicitado
                val productoEncontrado = respuesta.find { it.idproducto == idproducto }
                
                if (productoEncontrado != null) {
                    _uistate.value = ProductoDetalleUIState.Success(productoEncontrado)
                } else if (respuesta.isNotEmpty()) {
                    // Si no coincide exactamente pero hay datos, mostramos el primero por seguridad
                    _uistate.value = ProductoDetalleUIState.Success(respuesta[0])
                } else {
                    _uistate.value = ProductoDetalleUIState.Error("Producto no encontrado")
                }
            } catch (e: Exception) {
                _uistate.value = ProductoDetalleUIState.Error("Error al cargar detalle: ${e.localizedMessage}")
            }
        }
    }
}
