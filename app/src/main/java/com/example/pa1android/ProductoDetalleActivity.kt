package com.example.pa1android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.ui.theme.PA1AndroidTheme

class ProductoDetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val nombre = intent.getStringExtra("NOMBRE") ?: "Producto"
        val precio = intent.getIntExtra("PRECIO", 0)
        val imagenRes = intent.getIntExtra("IMAGEN_RES", R.drawable.img_zapato)
        val descripcion = intent.getStringExtra("DESCRIPCION") ?: ""

        setContent {
            PA1AndroidTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    ProductoDetalleContent(
                        nombre = nombre,
                        precio = precio,
                        imagenRes = imagenRes,
                        descripcion = descripcion,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoDetalleContent(
    nombre: String,
    precio: Int,
    imagenRes: Int,
    descripcion: String,
    modifier: Modifier = Modifier
) {
    val context = LocalActivity.current
    var selectedColor by remember { mutableStateOf("Negro") }
    var selectedSize by remember { mutableStateOf("37") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón cerrar
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { context?.finish() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
        }

        // Imagen del Producto
        Image(
            painter = painterResource(id = imagenRes),
            contentDescription = nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre y Precio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "S/ $precio.00",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
        }

        if (descripcion.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sección Colores
        Text(
            text = "Colores",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        val colores = listOf(
            "Negro" to Color.Black,
            "Gris" to Color.Gray,
            "Beige" to Color(0xFFD2B48C),
            "Blanco" to Color.White,
            "Marrón" to Color(0xFF8B4513)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(colores) { (name, color) ->
                ColorOption(
                    color = color,
                    name = name,
                    isSelected = selectedColor == name,
                    onClick = { selectedColor = name }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sección Tallas
        Text(
            text = "Tallas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        val tallas = (35..40).map { it.toString() }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tallas) { size ->
                SizeOption(
                    size = size,
                    isSelected = selectedSize == size,
                    onClick = { selectedSize = size }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón Comprar
        Button(
            onClick = { 
                val resultIntent = Intent().apply {
                    putExtra("NOMBRE", nombre)
                    putExtra("PRECIO", precio)
                    putExtra("IS_FAVORITE", true)
                }
                context?.setResult(Activity.RESULT_OK, resultIntent)
                context?.finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Comprar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ColorOption(color: Color, name: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color.LightGray.copy(alpha = 0.3f) else Color.Transparent,
        border = if (isSelected) BorderStroke(1.dp, Color.Black) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(1.dp, Color.LightGray, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, fontSize = 14.sp)
        }
    }
}

@Composable
fun SizeOption(size: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color.Black else Color.White)
            .border(1.dp, if (isSelected) Color.Black else Color.LightGray, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = size,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}
