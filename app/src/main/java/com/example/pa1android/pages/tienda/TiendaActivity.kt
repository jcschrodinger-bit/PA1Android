package com.example.pa1android.pages.tienda

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.pa1android.MainActivity
import com.example.pa1android.components.FloatingNavBar
import com.example.pa1android.models.Categoria
import com.example.pa1android.pages.Perfil.PerfilActivity
import com.example.pa1android.pages.compras.ComprasActivity
import com.example.pa1android.pages.productos.ProductosActivity
import com.example.pa1android.ui.theme.ui.theme.PA1AndroidTheme

/**
 * Pantalla Maestro de Categorías (llamada TiendaActivity para seguir el modelo Thor).
 */
class TiendaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[TiendaViewModel::class.java]
        viewModel.fetchTienda()

        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                val uiState by viewModel.uiState.collectAsState()

                Box(modifier = Modifier.fillMaxSize()) {
                    TiendaScreenContent(
                        uiState = uiState,
                        onCategoryClick = { categoria ->
                            val intent = Intent(this@TiendaActivity, ProductosActivity::class.java).apply {
                                putExtra("ID_CATEGORIA", categoria.id)
                                putExtra("NOMBRE_CATEGORIA", categoria.nombre)
                            }
                            startActivity(intent)
                        },
                        onNavigate = { target ->
                            when (target) {
                                "Home" -> startActivity(Intent(this@TiendaActivity, MainActivity::class.java))
                                "Compras" -> startActivity(Intent(this@TiendaActivity, ComprasActivity::class.java))
                                "Profile" -> startActivity(Intent(this@TiendaActivity, PerfilActivity::class.java))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TiendaScreenContent(
    uiState: TiendaUIState,
    onCategoryClick: (Categoria) -> Unit,
    onNavigate: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            com.example.pa1android.components.MyTopAppBar(titulo = "Categorías")

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Explora nuestras colecciones exclusivas",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                when (val state = uiState) {
                    is TiendaUIState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.Black)
                        }
                    }
                    is TiendaUIState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = state.message, color = Color.Red)
                        }
                    }
                    is TiendaUIState.Success -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            contentPadding = PaddingValues(bottom = 120.dp)
                        ) {
                            items(state.Categorias) { categoria ->
                                FilaTienda(categoria) { onCategoryClick(categoria) }
                            }
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            FloatingNavBar(currentScreen = "Productos", onNavigate = onNavigate)
        }
    }
}

@Composable
fun FilaTienda(categoria: Categoria, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = categoria.imagenRes),
                contentDescription = categoria.nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))
            Text(
                text = categoria.nombre,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
