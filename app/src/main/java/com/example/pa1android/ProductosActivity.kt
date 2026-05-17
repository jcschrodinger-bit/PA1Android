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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import coil.compose.AsyncImage
import com.example.pa1android.Network.APIConfig
import com.example.pa1android.Network.RetrofitClient
import com.example.pa1android.ui.components.FloatingNavBar
import com.example.pa1android.ui.theme.PA1AndroidTheme
import kotlinx.coroutines.launch

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                var selectedFilter by remember { mutableStateOf("Botas") }
                var listaCompletaProductos by remember { mutableStateOf<List<Producto>>(emptyList()) }
                var cargando by remember { mutableStateOf(true) }
                val coroutineScope = rememberCoroutineScope()

                // Lanzador para sincronizar compras al volver de la pantalla detalle
                val detailLauncher = rememberLauncherForActivityResult(
                    contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
                ) { _ -> }

                // Consumo asíncrono desde el hosting AlwaysData (Equivalente al .task de tu iOS)
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        try {
                            cargando = true
                            // Llamada nativa a productos.php mediante la interfaz Retrofit
                            val resultado = RetrofitClient.apiService.obtenerProductos()
                            listaCompletaProductos = resultado
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            cargando = false
                        }
                    }
                }

                // Mapeo lógico de categorías dinámicas requerido por rúbrica: 1=Botas, 2=Zapatillas, 3=Tacos, 4=Sandalias
                val idCategoriaFiltro = when (selectedFilter) {
                    "Botas" -> "1"
                    "Zapatillas" -> "2"
                    "Tacos" -> "3"
                    "Sandalias" -> "4"
                    else -> "1"
                }

                // Filtrado reactivo en caliente de los 39 productos según la pestaña activa
                val productosFiltrados = listaCompletaProductos.filter { it.id_categoria == idCategoriaFiltro }

                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    Scaffold(
                        containerColor = Color.Transparent,
                        topBar = {
                            Column(modifier = Modifier.padding(top = 48.dp, bottom = 16.dp)) {
                                Text(
                                    text = "Elegance",
                                    style = MaterialTheme.typography.displayMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                FilterRow(selectedFilter) { selectedFilter = it }
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
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator(color = Color.Black)
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(text = "Cargando catálogo...", color = Color.Gray, fontSize = 14.sp)
                                    }
                                }
                            } else {
                                // Cuadrícula de 2 columnas obligatoria según lineamientos visuales
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(24.dp),
                                    contentPadding = PaddingValues(bottom = 120.dp)
                                ) {
// Dentro de la cuadrícula LazyVerticalGrid de ProductosActivity.kt
                                    items(productosFiltrados) { producto ->
                                        ProductCard(
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
                                        }
                                    }
                                }
                            }
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

@Composable
fun FilterRow(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Botas", "Zapatillas", "Tacos", "Sandalias")
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Black,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    labelColor = Color.Black
                ),
                border = null,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun ProductCard(
    producto: Producto,
    isInitiallyFavorite: Boolean = false,
    onFavoriteChanged: (Boolean) -> Unit = {},
    onClick: () -> Unit
) {
    var isFavorite by remember(isInitiallyFavorite) { mutableStateOf(isInitiallyFavorite) }

    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            // Replicación de AsyncImage usando Coil para renderizar desde URL de AlwaysData
            AsyncImage(
                model = APIConfig.getImagenURL(producto.imagen ?: ""),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    onFavoriteChanged(isFavorite)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = producto.nombre ?: "",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 1
        )

        val precioDouble = producto.precioBase ?: 0.0
        Text(
            text = "S/ ${String.format("%.2f", precioDouble)}", // Formateo premium (S/ 0.00) solicitado por rúbrica
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}