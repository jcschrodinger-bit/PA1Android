package com.example.pa1android.ui.productos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.pa1android.ui.theme.PA1AndroidTheme

/**
 * Actividad encargada de recibir los parámetros y arrancar la pantalla de productos.
 * Sigue el patrón MVVM separando la vista de la lógica.
 */
class ProductosActivity : ComponentActivity() {
    
    // El ViewModel se crea automáticamente usando este delegado oficial de Android
    private val viewModel: ProductosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Recibimos el ID y el Nombre de la categoría desde la pantalla anterior (Maestro)
        val idCategoria = intent.getStringExtra("ID_CATEGORIA") ?: "1"
        val nombreCategoria = intent.getStringExtra("NOMBRE_CATEGORIA") ?: "Elegance"

        // 2. Le pedimos al ViewModel que cargue los datos (Filtrado en servidor)
        viewModel.cargarProductos(idCategoria)

        setContent {
            PA1AndroidTheme {
                // 3. Dibujamos la pantalla pasando el nombre y el ViewModel
                ProductosScreen(
                    nombreCategoria = nombreCategoria,
                    viewModel = viewModel
                )
            }
        }
    }
}
