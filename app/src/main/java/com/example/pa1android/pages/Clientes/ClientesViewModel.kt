package com.example.pa1android.pages.Clientes

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

class ClientesViewModel : ViewModel() {
    private val _uistate = MutableStateFlow<ClientesUIState>(ClientesUIState.Loading)
    val uiState : StateFlow<ClientesUIState> = _uistate.asStateFlow()

    var correotelefono by mutableStateOf("")
    var clave by mutableStateOf("")

    var estadoCheck by mutableStateOf(false)

    fun fetchLogin(){
        viewModelScope.launch {
            _uistate.value = ClientesUIState.Loading
            try{
                val respuesta = RetrofitClient.loginService.getLogin(correotelefono, clave)
                _uistate.value = ClientesUIState.Success(respuesta)
            } catch(e: Exception){
                _uistate.value = ClientesUIState.Error("Error al cargar los datos: ${e.localizedMessage}")
            }
        }
    }
}
