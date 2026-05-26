package com.example.pa1android.ui.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pa1android.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * El cerebro de la pantalla de productos.
 * Se encarga de pedir los datos al servidor y manejar el estado.
 */
class ProductosViewModel : ViewModel() {

    // El estado interno que solo el ViewModel puede cambiar
    private val _uiState = MutableStateFlow(ProductosUiState())
    // El estado que la pantalla puede leer para dibujarse
    val uiState: StateFlow<ProductosUiState> = _uiState.asStateFlow()

    /**
     * Función que hace el viaje al servidor para traer los productos.
     * Recibe el ID de la categoría para traer solo lo que necesitamos.
     */
    fun cargarProductos(idCategoria: String) {
        viewModelScope.launch {
            try {
                // Indicamos que empezamos a cargar
                _uiState.value = _uiState.value.copy(estaCargando = true)
                
                // Pedimos a internet solo los productos de esa categoría (Filtrado en servidor)
                val resultado = RetrofitClient.apiService.obtenerProductos(idCategoria)
                
                // Guardamos el resultado en el estado
                _uiState.value = _uiState.value.copy(
                    listaProductos = resultado,
                    estaCargando = false
                )
            } catch (e: Exception) {
                // Si hubo un error, lo anotamos
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    mensajeError = "No se pudieron cargar los productos"
                )
            }
        }
    }
}
