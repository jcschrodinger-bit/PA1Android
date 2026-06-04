package com.example.pa1android.pages.Clientes

sealed interface ClientesUIState {
    data object Loading : ClientesUIState
    data class Success(val resultado: String) : ClientesUIState
    data class Error(val message: String): ClientesUIState
}
