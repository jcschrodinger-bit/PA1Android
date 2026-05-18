package com.example.pa1android

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.ui.components.FloatingNavBar
import com.example.pa1android.ui.theme.PA1AndroidTheme

// Modelo simple para las categorías
data class CategoriaElegance(
    val id: String,
    val nombre: String,
    val imagenRes: Int
)

class CategoriasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                Box( // Contenedor para apilar cosas una encima de otra
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    CategoriasScreen(onCategoryClick = { id ->
                        // Navegación Maestro-Detalle: enviamos el ID a la pantalla de productos
                        val intent = Intent(this@CategoriasActivity, ProductosActivity::class.java).apply {
                            putExtra("ID_CATEGORIA", id)
                        }
                        startActivity(intent)
                    })

                    Box(modifier = Modifier.align(Alignment.BottomCenter)) { // Contenedor para la barra inferior
                        FloatingNavBar(currentScreen = "Productos", onNavigate = { target ->
                            when (target) {
                                "Home" -> startActivity(Intent(this@CategoriasActivity, MainActivity::class.java))
                                "Compras" -> startActivity(Intent(this@CategoriasActivity, ComprasActivity::class.java))
                                "Profile" -> startActivity(Intent(this@CategoriasActivity, PerfilActivity::class.java))
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriasScreen(onCategoryClick: (String) -> Unit) {
    val categorias = listOf(
        CategoriaElegance("1", "Botas", R.drawable.img_zapato),
        CategoriaElegance("2", "Zapatillas", R.drawable.img_portada),
        CategoriaElegance("3", "Tacos", R.drawable.img_compra),
        CategoriaElegance("4", "Sandalias", R.drawable.img_bienvenida)
    )

    Column( // Contenedor para poner cosas una debajo de otra
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Explora nuestras colecciones exclusivas",
            color = Color.Gray,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        LazyColumn( // Lista desplazable para las categorías
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(categorias) { categoria -> // Repetir para cada categoría
                CategoryCard(categoria) { onCategoryClick(categoria.id) }
            }
        }
    }
}

@Composable
fun CategoryCard(categoria: CategoriaElegance, onClick: () -> Unit) {
    Card( // Tarjeta o superficie de diseño
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) { // Contenedor para la imagen y el texto
            Image( // Imagen de alta calidad
                painter = painterResource(id = categoria.imagenRes),
                contentDescription = categoria.nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Capa oscura para que el texto se lea bien
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            
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
