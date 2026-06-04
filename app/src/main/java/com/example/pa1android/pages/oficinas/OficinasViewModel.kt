package com.example.pa1android.pages.oficinas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pa1android.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OficinasViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<OficinasUIState>(OficinasUIState.Loading)
    val uiState: StateFlow<OficinasUIState> = _uiState.asStateFlow()

    var idoficina by mutableStateOf("")
    var nombre by mutableStateOf("")
    var ciudad by mutableStateOf("")

    init {
        fetchOficinas()
    }

    fun fetchOficinas() {
        viewModelScope.launch {
            _uiState.value = OficinasUIState.Loading
            try {
                val respuesta = RetrofitClient.oficinasService.getOficinas()
                _uiState.value = OficinasUIState.Success(respuesta)
            } catch (e: Exception) {
                _uiState.value = OficinasUIState.Error("Error al cargar oficinas: ${e.localizedMessage}")
            }
        }
    }

    fun insertOficina() {
        viewModelScope.launch {
            try {
                RetrofitClient.oficinasService.insertOficina(nombre, ciudad)
                nombre = ""
                ciudad = ""
                fetchOficinas()
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun updateOficina() {
        viewModelScope.launch {
            try {
                RetrofitClient.oficinasService.updateOficina(idoficina, nombre, ciudad)
                nombre = ""
                ciudad = ""
                fetchOficinas()
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
