package com.example.pa1android.ui.productos

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.data.local.CartManager
import com.example.pa1android.ui.activities.MainActivity
import com.example.pa1android.data.network.APIConfig
import com.example.pa1android.ui.activities.ProductoDetalleActivity
import com.example.pa1android.ui.activities.PerfilActivity
import com.example.pa1android.ui.activities.ComprasActivity
import com.example.pa1android.ui.components.FloatingNavBar
import com.example.pa1android.ui.components.ProductCard

/**
 * Código visual de la pantalla de productos.
 * Reacciona automáticamente a lo que diga el ViewModel.
 */
@Composable
fun ProductosScreen(
    nombreCategoria: String, // Recibido desde la pantalla Maestra
    viewModel: ProductosViewModel
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    val detailLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ -> }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { // Contenedor principal
        Scaffold( // Estructura básica
            containerColor = Color.Transparent,
            topBar = {
                Column(modifier = Modifier.padding(top = 48.dp, bottom = 16.dp)) { // Contenedor vertical
                    Text(
                        text = "Colección $nombreCategoria", // Título dinámico
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) { // Contenedor para el contenido
                if (state.estaCargando) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { // Contenedor centrado
                        CircularProgressIndicator(color = Color.Black)
                    }
                } else if (state.mensajeError != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { // Contenedor centrado
                        Text(text = state.mensajeError!!, color = Color.Red)
                    }
                } else {
                    LazyVerticalGrid( // Lista desplazable en cuadrícula
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(bottom = 120.dp)
                    ) {
                        items(state.listaProductos) { producto -> // Repetir para cada producto
                            ProductCard( // Tarjeta del producto
                                producto = producto,
                                isInitiallyFavorite = CartManager.isSelected(producto.nombre ?: ""),
                                onFavoriteChanged = {
                                    val precioInt = producto.precioBase?.toInt() ?: 0
                                    CartManager.toggleProduct(producto.nombre ?: "Producto", precioInt)
                                }
                            ) {
                                val intent = Intent(context, ProductoDetalleActivity::class.java).apply {
                                    putExtra("NOMBRE", producto.nombre ?: "Producto Elegance")
                                    putExtra("PRECIO", producto.precioBase?.toInt() ?: 0)
                                    putExtra("IMAGEN_URL", APIConfig.getImagenURL(producto.imagen ?: ""))
                                    putExtra("DESCRIPCION", producto.descripcion ?: "")
                                    putExtra("ID_CATEGORIA", producto.idCategoria ?: 1)
                                }
                                detailLauncher.launch(intent)
                            }
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) { // Barra de navegación inferior
            FloatingNavBar(currentScreen = "Productos", onNavigate = { target ->
                when (target) {
                    "Home" -> context.startActivity(Intent(context, MainActivity::class.java))
                    "Compras" -> context.startActivity(Intent(context, ComprasActivity::class.java))
                    "Profile" -> context.startActivity(Intent(context, PerfilActivity::class.java))
                }
            })
        }
    }
}
