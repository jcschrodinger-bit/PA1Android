package com.example.pa1android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.Network.APIConfig
import com.example.pa1android.Network.RetrofitClient
import com.example.pa1android.ui.components.FilterRow
import com.example.pa1android.ui.components.FloatingNavBar
import com.example.pa1android.ui.components.ProductCard
import com.example.pa1android.ui.theme.PA1AndroidTheme
import kotlinx.coroutines.launch

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val idCategoriaInicial = intent.getStringExtra("ID_CATEGORIA") ?: "1"
        
        setContent {
            PA1AndroidTheme {
                var selectedFilter by remember { 
                    mutableStateOf(
                        when(idCategoriaInicial) {
                            "1" -> "Botas"
                            "2" -> "Zapatillas"
                            "3" -> "Tacos"
                            "4" -> "Sandalias"
                            else -> "Botas"
                        }
                    )
                }
                var listaCompletaProductos by remember { mutableStateOf<List<Producto>>(emptyList()) }
                var cargando by remember { mutableStateOf(true) }
                val coroutineScope = rememberCoroutineScope()

                // Lanzador para sincronizar compras al volver de la pantalla detalle
                val detailLauncher = rememberLauncherForActivityResult(
                    contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
                ) { _ -> }

                // Consumo asíncrono desde el hosting AlwaysData
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        try {
                            cargando = true
                            val resultado = RetrofitClient.apiService.obtenerProductos()
                            listaCompletaProductos = resultado
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            cargando = false
                        }
                    }
                }

                // Categorías requerido por rúbrica
                val idCategoriaFiltro = when (selectedFilter) {
                    "Botas" -> "1"
                    "Zapatillas" -> "2"
                    "Tacos" -> "3"
                    "Sandalias" -> "4"
                    else -> "1"
                }

                // Filtrado reactivo en caliente de los 39 productos según la pestaña activa
                val productosFiltrados = listaCompletaProductos.filter { it.id_categoria == idCategoriaFiltro }

                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { // Contenedor principal
                    Scaffold( // Estructura básica con barra superior e inferior
                        containerColor = Color.Transparent,
                        topBar = {
                            Column(modifier = Modifier.padding(top = 48.dp, bottom = 16.dp)) { // Contenedor vertical
                                Text(
                                    text = "Elegance",
                                    style = MaterialTheme.typography.displayMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                FilterRow(selectedFilter) { selectedFilter = it } // Fila de filtros
                            }
                        }
                    ) { innerPadding ->
                        // Transición fluida con desvanecimiento animado al alternar filtros
                        AnimatedContent(
                            targetState = selectedFilter,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                            },
                            label = "FilterTransition",
                            modifier = Modifier.padding(innerPadding)
                        ) { _ ->
                            if (cargando) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { // Contenedor centrado
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) { // Contenedor vertical
                                        CircularProgressIndicator(color = Color.Black)
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(text = "Cargando catálogo...", color = Color.Gray, fontSize = 14.sp)
                                    }
                                }
                            } else {
                                // La organizamos en 2 columnas
                                LazyVerticalGrid( // Lista en forma de cuadrícula
                                    columns = GridCells.Fixed(2), // 2 columnas
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(24.dp),
                                    contentPadding = PaddingValues(bottom = 120.dp)
                                ) {

                                    items(productosFiltrados) { producto -> // Repetir por cada producto
                                        ProductCard( // Tarjeta individual del producto
                                            producto = producto,
                                            isInitiallyFavorite = CartManager.isSelected(producto.nombre ?: ""),
                                            onFavoriteChanged = {
                                                val precioInt = producto.precioBase?.toInt() ?: 0
                                                CartManager.toggleProduct(producto.nombre ?: "Producto", precioInt)
                                            }
                                        ) {
                                            val intent = Intent(this@ProductosActivity, ProductoDetalleActivity::class.java).apply {
                                                putExtra("NOMBRE", producto.nombre ?: "Producto Elegance")
                                                putExtra("PRECIO", producto.precioBase?.toInt() ?: 0)
                                                putExtra("IMAGEN_URL", APIConfig.getImagenURL(producto.imagen ?: ""))
                                                putExtra("DESCRIPCION", producto.descripcion ?: "")
                                                putExtra("ID_CATEGORIA", producto.idCategoria ?: 1)
                                            }
                                            detailLauncher.launch(intent)
                                        } // ProductCard
                                    } // Items
                                } // LazyVerticalGrid
                            } // else
                        }
                    }

                    // Renderizado del componente flotante reutilizable acoplado a la pestaña activa
                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        FloatingNavBar(currentScreen = "Productos", onNavigate = { target ->
                            when (target) {
                                "Home" -> startActivity(Intent(this@ProductosActivity, MainActivity::class.java))
                                "Compras" -> startActivity(Intent(this@ProductosActivity, ComprasActivity::class.java))
                                "Profile" -> startActivity(Intent(this@ProductosActivity, PerfilActivity::class.java))
                            }
                        })
                    }
                }
            }
        }
    }
}