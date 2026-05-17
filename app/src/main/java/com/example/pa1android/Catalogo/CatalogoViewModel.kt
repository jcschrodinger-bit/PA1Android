package com.example.pa1android.Catalogo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pa1android.Producto
import com.example.pa1android.Network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    // Almacén reactivo privado de productos remoto (Equivalente al @Published listaProductos)
    private val _listaProductos = MutableStateFlow<List<Producto>>(emptyList())

    // Bandera de control de red (Equivalente a @Published var cargando)
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Filtro activo mutable expuesto de forma directa (Equivalente a @Published selectedFilter)
    var selectedFilter by mutableStateOf("Botas")

    /**
     * Propiedad calculada dinámica en caliente para filtrar las 39 filas de datos.
     * Mapea rigurosamente los identificadores del hosting AlwaysData.
     */
    val currentList: List<Producto>
        get() {
            val idCategoria = when (selectedFilter) {
                "Botas" -> "1"
                "Zapatillas" -> "2"
                "Tacos" -> "3"
                "Sandalias" -> "4"
                else -> "1"
            }
            return _listaProductos.value.filter { it.id_categoria == idCategoria }
        }

    /**
     * Consume de manera asíncrona la lista de productos en segundo plano.
     * Incorpora el bloqueo de concurrencia emulando tu lógica de Xcode.
     */
    fun cargarProductos() {
        if (_cargando.value) return // Bloqueo de peticiones duplicadas en ráfaga

        viewModelScope.launch {
            try {
                _cargando.value = true
                val resultado = RetrofitClient.apiService.obtenerProductos()
                _listaProductos.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _cargando.value = false
            }
        }
    }
}