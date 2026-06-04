package com.example.pa1android.pages.oficinas

import com.example.pa1android.models.Oficina

sealed interface OficinasUIState {
    data object Loading : OficinasUIState
    data class Success(val oficinas: List<Oficina>) : OficinasUIState
    data class Error(val message: String) : OficinasUIState
}
