package com.example.pa1android.ui.productos

import com.example.pa1android.data.model.Producto

/**
 * Clase que guarda el estado de la pantalla de productos.
 * Representa lo que el usuario ve en cada momento.
 */
data class ProductosUiState(
    val listaProductos: List<Producto> = emptyList(), // Lista de productos que vienen del servidor
    val estaCargando: Boolean = false,                // Si estamos esperando la respuesta de internet
    val mensajeError: String? = null                  // Si algo salió mal en la descarga
)
