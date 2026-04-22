package com.example.pa1android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.pa1android.ui.theme.PA1AndroidTheme

data class Producto(val nombre: String, val precio: Int, val imagenRes: Int, val descripcion: String = "")

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                var selectedFilter by remember { mutableStateOf("Botas") }
                val favoritos = remember { mutableStateMapOf<String, Boolean>() }
                
                // Configurar launcher para recibir resultado de Detalle
                val detailLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                    contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == android.app.Activity.RESULT_OK) {
                        val nombreProducto = result.data?.getStringExtra("NOMBRE")
                        val isFavorite = result.data?.getBooleanExtra("IS_FAVORITE", false) ?: false
                        if (nombreProducto != null) {
                            favoritos[nombreProducto] = isFavorite
                        }
                    }
                }

                // Definición de las 10 botas completas con sus descripciones
                val listaBotas = listOf(
                    Producto("Sienne Étoile", 240, R.drawable.sienne_etoile, "Esta bota over-the-knee es la pieza definitiva para la mujer que desea una silueta infinita y una presencia magnética. Su diseño artesanal de textura aterciopelada captura la luz, logrando el equilibrio ideal entre la calidez estacional y el glamour de las pasarelas internacionales."),
                    Producto("Bordeaux Cavalière", 260, R.drawable.bordeaux_cavaliere, "Inspirada en la estética ecuestre de la alta sociedad, esta bota de grano entero redefine la elegancia funcional con un brillo sofisticado y duradero. Su diseño clásico transita sin esfuerzo hacia eventos de alto nivel, siendo ideal para quien busca nobleza en los materiales y una autoridad femenina impecable."),
                    Producto("Noir Impérial", 280, R.drawable.noir_imperial, "Una obra de arquitectura minimalista diseñada para la mujer vanguardista que entiende que el lujo reside en la perfección de la línea. Su tacón geométrico y puntera estilizada ofrecen una visión cosmopolita, convirtiéndola en el complemento iconográfico indispensable para proyectar éxito con una audacia silenciosa."),
                    Producto("Chocolat Velours Comfort", 220, R.drawable.chocolat_velours_comfort, "Este modelo representa el \"lujo silencioso\", fusionando una densidad textil superior con un forro que garantiza una calidez inigualable sin sacrificar la distinción. Es la opción definitiva para la mujer que exige el máximo confort y una estética de élite incluso en los climas más exigentes."),
                    Producto("Charbon Architech", 250, R.drawable.charbon_architech, "La fusión perfecta de ingeniería vanguardista y sofisticación, con una caña que se adapta como una segunda piel. Su tacón con acabado metálico añade un toque arquitectónico que eleva cualquier conjunto, garantizando una silueta impecable para la mujer con una visión de poder."),
                    Producto("Émeraude Chelsea", 270, R.drawable.emeraude_chelsea, "Una reinterpretación lujosa del diseño clásico, creada para quienes entienden que la distinción reside en la audacia de los materiales nobles. Con detalles satinados y un tacón de madera tratada, ofrece una sofisticación moderna y relajada para los entornos urbanos más exclusivos."),
                    Producto("Expresso Lacet", 230, R.drawable.expresso_lacet, "Esencia de elegancia artesanal que combina cuero graneado con un intrincado detalle de cordones grabados con láser. Su estructura equilibrada ofrece una estabilidad envidiable sin perder sofisticación, siendo el calzado que comunica exclusividad y un estilo atemporal hecho a medida."),
                    Producto("Nuit Velours Étoile", 290, R.drawable.nuit_velours_etoile, "El epítome de la elegancia nocturna, diseñada para ocasiones donde cada detalle cuenta y la pierna se envuelve como una joya. Su intrincado bordado de filigrana asegura que serás el centro de todas las miradas en eventos de alfombra roja o estrenos de ópera."),
                    Producto("Botte Exotique Caramel", 310, R.drawable.botte_exotique_caramel, "Definición de elegancia texturizada para la mujer que busca un lujo exótico y refinado en su calzado. Su patrón grabado y herrajes de distinción aportan una altura imponente y un aire aristocrático, convirtiéndola en una pieza indispensable de cualquier guardarropa de alto nivel."),
                    Producto("Botte Étoile de Minuit", 330, R.drawable.botte_etoile_de_minuit, "Una silueta estilizada que captura la esencia de la distinción nocturna mediante un acabado aterciopelado y bordados de alta costura. Diseñada para ofrecer un glamour absoluto, su perfil sofisticado es la elección predilecta para la mujer cosmopolita que no acepta menos que la perfección.")
                )

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
                        AnimatedContent(
                            targetState = selectedFilter,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                            },
                            label = "FilterTransition",
                            modifier = Modifier.padding(innerPadding)
                        ) { targetFilter ->
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp),
                                contentPadding = PaddingValues(bottom = 120.dp)
                            ) {
                                if (targetFilter == "Botas") {
                                    items(listaBotas.size) { index ->
                                        val producto = listaBotas[index]
                                        ProductCard(
                                            producto = producto,
                                            isInitiallyFavorite = favoritos[producto.nombre] ?: false,
                                            onFavoriteChanged = { fav -> favoritos[producto.nombre] = fav }
                                        ) {
                                            val intent = Intent(this@ProductosActivity, ProductoDetalleActivity::class.java).apply {
                                                putExtra("NOMBRE", producto.nombre)
                                                putExtra("PRECIO", producto.precio)
                                                putExtra("IMAGEN_RES", producto.imagenRes)
                                                putExtra("DESCRIPCION", producto.descripcion)
                                            }
                                            detailLauncher.launch(intent)
                                        }
                                    }
                                } else {
                                    // Espacios reservados para las otras categorías
                                    items(10) { index ->
                                        val dummyProducto = Producto("Elegance $targetFilter ${index + 1}", 150 + (index * 10), R.drawable.img_zapato)
                                        ProductCard(
                                            producto = dummyProducto,
                                            isInitiallyFavorite = favoritos[dummyProducto.nombre] ?: false,
                                            onFavoriteChanged = { fav -> favoritos[dummyProducto.nombre] = fav }
                                        ) {
                                            val intent = Intent(this@ProductosActivity, ProductoDetalleActivity::class.java).apply {
                                                putExtra("NOMBRE", dummyProducto.nombre)
                                                putExtra("PRECIO", dummyProducto.precio)
                                                putExtra("IMAGEN_RES", dummyProducto.imagenRes)
                                            }
                                            detailLauncher.launch(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        FloatingNavBar(currentScreen = "Productos", onNavigate = { target ->
                            when(target) {
                                "Home" -> startActivity(Intent(this@ProductosActivity, MainActivity::class.java))
                                "Compras" -> startActivity(Intent(this@ProductosActivity, ComprasActivity::class.java))
                                "Contacto" -> startActivity(Intent(this@ProductosActivity, ContactoActivity::class.java))
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

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = producto.imagenRes),
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
            text = producto.nombre,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 1
        )
        
        if (producto.precio > 0) {
            Text(
                text = "S/ ${producto.precio}.00",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
