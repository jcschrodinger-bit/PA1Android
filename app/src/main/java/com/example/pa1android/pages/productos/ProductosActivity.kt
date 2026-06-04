package com.example.pa1android.pages.productos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.pa1android.MainActivity
import com.example.pa1android.components.FloatingNavBar
import com.example.pa1android.data.local.CartManager
import com.example.pa1android.pages.Perfil.PerfilActivity
import com.example.pa1android.pages.compras.ComprasActivity
import com.example.pa1android.pages.productodetalle.ProductoDetalleActivity
import com.example.pa1android.ui.theme.ui.theme.PA1AndroidTheme
import com.example.pa1android.utils.getImagenURL

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[ProductosViewModel::class.java]

        val bundle = intent.extras
        val idcategoriaStr = bundle?.getString("ID_CATEGORIA") ?: "1"
        val idcategoria = idcategoriaStr.toInt()
        val nombre = bundle?.getString("NOMBRE_CATEGORIA") ?: "Elegance"

        viewModel.fetchProductos(idcategoria)

        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                val uiState by viewModel.uiState.collectAsState()
                
                ProductosScreenContent(
                    nombreCategoria = nombre,
                    uiState = uiState,
                    onBackToHome = { startActivity(Intent(this, MainActivity::class.java)) },
                    onGoToCompras = { startActivity(Intent(this, ComprasActivity::class.java)) },
                    onGoToProfile = { startActivity(Intent(this, PerfilActivity::class.java)) },
                    onSelectProducto = { idproducto -> seleccionarProducto(idproducto) },
                    onRetry = { viewModel.fetchProductos(idcategoria) }
                )
            }
        }
    }

    private fun seleccionarProducto(idproducto: Int) {
        val intent = Intent(this, ProductoDetalleActivity::class.java)
        val bundle = Bundle().apply {
            putInt("ID_PRODUCTO", idproducto)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

@Composable
fun ProductosScreenContent(
    nombreCategoria: String,
    uiState: ProductosUIState,
    onBackToHome: () -> Unit,
    onGoToCompras: () -> Unit,
    onGoToProfile: () -> Unit,
    onSelectProducto: (Int) -> Unit,
    onRetry: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                com.example.pa1android.components.MyTopAppBar(titulo = "Colección $nombreCategoria")
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (val state = uiState) {
                        is ProductosUIState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.Black)
                        }

                        is ProductosUIState.Error -> {
                            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = state.message, color = MaterialTheme.colorScheme.error)
                                Button(onClick = onRetry) {
                                    Text("Reintentar")
                                }
                            }
                        }

                        is ProductosUIState.Success -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp),
                                contentPadding = PaddingValues(bottom = 120.dp)
                            ) {
                                items(items = state.Productos, key = { it.idproducto }) { itemProducto ->
                                    Card(
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        colors = CardDefaults.cardColors(Color.White),
                                        modifier = Modifier.fillMaxWidth()
                                            .clickable { onSelectProducto(itemProducto.idproducto) }
                                    ) {
                                        FilaProducto(
                                            itemProducto = itemProducto,
                                            isInitiallyFavorite = CartManager.isSelected(itemProducto.nombre),
                                            onFavoriteChanged = {
                                                CartManager.toggleProduct(itemProducto.nombre, itemProducto.precio.toInt())
                                            },
                                            onClick = { onSelectProducto(itemProducto.idproducto) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            FloatingNavBar(currentScreen = "Productos", onNavigate = { target ->
                when (target) {
                    "Home" -> onBackToHome()
                    "Compras" -> onGoToCompras()
                    "Profile" -> onGoToProfile()
                }
            })
        }
    }
}
