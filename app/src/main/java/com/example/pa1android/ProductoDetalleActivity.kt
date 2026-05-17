package com.example.pa1android

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.pa1android.ui.theme.PA1AndroidTheme

// Estructura espejo para resolver la lógica especial de Sandalias (id_categoria == 4)
data class EleganceColorResult(val nombre: String, val color: Color)

fun obtenerColorSandalia(nombreProducto: String): EleganceColorResult {
    val nombre = nombreProducto.lowercase().trim()
    return when {
        nombre.contains("sky blue") || nombre.contains("azur") -> EleganceColorResult("Azul Cielo", Color(0xFF87CEEB))
        nombre.contains("émeraude") -> EleganceColorResult("Verde Esmeralda", Color(0xFF009966))
        nombre.contains("tournesol") -> EleganceColorResult("Amarillo Girasol", Color(0xFFFFCC00))
        nombre.contains("rouge passion") -> EleganceColorResult("Rojo Pasión", Color(0xFFD32F2F))
        nombre.contains("fuchsia") -> EleganceColorResult("Fucsia Vibrante", Color(0xFFE91E63))
        nombre.contains("turquoise") -> EleganceColorResult("Turquesa Caribe", Color(0xFF40E0D0))
        nombre.contains("vibrant orange") -> EleganceColorResult("Naranja Vibrante", Color(0xFFFF9800))
        nombre.contains("orchid purple") -> EleganceColorResult("Púrpura Orquídea", Color(0xFF9C27B0))
        else -> EleganceColorResult("Color Único", Color.Black)
    }
}

class ProductoDetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Recuperación de los parámetros de producción enviados desde el catálogo
        val nombre = intent.getStringExtra("NOMBRE") ?: "Producto"
        val precio = intent.getIntExtra("PRECIO", 0)
        val imagenUrl = intent.getStringExtra("IMAGEN_URL") ?: ""
        val descripcion = intent.getStringExtra("DESCRIPCION") ?: "Diseño sofisticado."
        val idCategoria = intent.getIntExtra("ID_CATEGORIA", 1)

        setContent {
            PA1AndroidTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background // Fondo LightGray de la rúbrica
                ) { innerPadding ->
                    ProductoDetalleScreen(
                        nombre = nombre,
                        precio = precio,
                        imagenUrl = imagenUrl,
                        descripcion = descripcion,
                        idCategoria = idCategoria,
                        onClose = { finish() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoDetalleScreen(
    nombre: String,
    precio: Int,
    imagenUrl: String,
    descripcion: String,
    idCategoria: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estados internos reactivos idénticos a las variables @State de tu iOS
    var tallaSeleccionada by remember { mutableStateOf("38") }
    var colorSeleccionado by remember { mutableStateOf("Negro") }

    val isInCart = CartManager.isSelected(nombre)
    val datosColorSandalia = remember(nombre) { obtenerColorSandalia(nombre) }

    // Sincroniza el color seleccionado por defecto en el arranque si es sandalia
    LaunchedEffect(idCategoria) {
        if (idCategoria == 4) {
            colorSeleccionado = datosColorSandalia.nombre
        }
    }

    // ScrollView vertical nativo equivalente al ScrollView de Xcode
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ==========================================
        // BOTÓN DE CIERRE (La "X" en la parte superior derecha)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.05f), CircleShape)
                    .size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.Black
                )
            }
        }

        // ==========================================
        // IMAGEN DEL PRODUCTO (Carga remota asíncrona mediante Coil)
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.Black.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // NOMBRE Y PRECIO FORMATEADO
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "S/ ${String.format("%.2f", precio.toDouble())}",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // DESCRIPCIÓN DEL CALZADO
        Text(
            text = descripcion,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ==========================================
        // SECCIÓN: SELECCIONA TU TALLA
        // ==========================================
        Text(
            text = "Selecciona tu Talla",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Carrusel horizontal de tallas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val tallas = listOf("36", "37", "38", "39", "40")
            tallas.forEach { talla ->
                val esTallaActiva = tallaSeleccionada == talla
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (esTallaActiva) Color.Black else Color.White)
                        .border(1.dp, Color.Gray.copy(alpha = 0.2f), CircleShape)
                        .clickable { tallaSeleccionada = talla },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = talla,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (esTallaActiva) Color.White else Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // SECCIÓN: COLOR (Lógica Especial Requerida por Rúbrica)
        // ==========================================
        Text(
            text = "Color",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
            if (idCategoria == 4) {
                // MODO SANDALIAS: Renderizado dinámico del color real extraído del texto
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(datosColorSandalia.color)
                            .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
                    )
                    Text(
                        text = datosColorSandalia.nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else {
                // MODO CLÁSICO: Selectores interactivos circulares con aro de contorno
                Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    val coloresClasicos = listOf(
                        "Negro" to Color.Black,
                        "Marrón" to Color(0xFF8B4513),
                        "Gris" to Color.Gray
                    )
                    coloresClasicos.forEach { (nombreColor, colorObjeto) ->
                        val esColorActivo = colorSeleccionado == nombreColor
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(colorObjeto)
                                .border(
                                    width = if (esColorActivo) 2.dp else 0.dp,
                                    color = if (esColorActivo) Color.Black else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { colorSeleccionado = nombreColor }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // ==========================================
        // BOTÓN INTERACTIVO DE COMPRA / ELIMINACIÓN
        // ==========================================
        Button(
            onClick = {
                // Agrega o remueve el producto usando tu lógica persistente de llaves compuestas
                CartManager.toggleProduct(nombre, precio)
                onClose() // Regresa al catálogo de forma fluida
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isInCart) Color(0xFFD32F2F) else Color.Black
            ),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                text = if (isInCart) "Quitar del carrito" else "Comprar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}