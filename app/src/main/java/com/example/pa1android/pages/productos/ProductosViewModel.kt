package com.example.pa1android.pages.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pa1android.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que sigue la lógica exacta de ProyectoThor.
 */
class ProductosViewModel: ViewModel() {
    private val _uistate = MutableStateFlow<ProductosUIState>(ProductosUIState.Loading)
    val uiState : StateFlow<ProductosUIState> = _uistate.asStateFlow()

    fun fetchProductos(idcategoria: Int){
        viewModelScope.launch {
            _uistate.value = ProductosUIState.Loading
            try{
                // Llamada al servicio web filtrando por categoría
                val respuesta = RetrofitClient.productosService.getProductos(idcategoria)
                _uistate.value = ProductosUIState.Success(respuesta)
            } catch(e: Exception){
                _uistate.value = ProductosUIState.Error("Error al cargar los datos: ${e.localizedMessage}")
            }
        }
    }
}
