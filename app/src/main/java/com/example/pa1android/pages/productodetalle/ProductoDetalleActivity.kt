package com.example.pa1android.pages.productodetalle

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.pa1android.data.local.CartManager
import com.example.pa1android.models.Producto
import com.example.pa1android.ui.theme.ui.theme.PA1AndroidTheme
import com.example.pa1android.utils.getImagenURL
import java.util.Locale

class ProductoDetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val viewModel = ViewModelProvider(this)[ProductoDetalleViewModel::class.java]
        val bundle = intent.extras
        val idproducto = bundle?.getInt("ID_PRODUCTO") ?: 1
        
        viewModel.fetchProductoDetalle(idproducto)

        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                val uiState by viewModel.uiState.collectAsState()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background 
                ) { innerPadding ->
                    when (val state = uiState) {
                        is ProductoDetalleUIState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color.Black)
                            }
                        }
                        is ProductoDetalleUIState.Error -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = state.message, color = Color.Red)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { finish() }) {
                                        Text("Regresar")
                                    }
                                }
                            }
                        }
                        is ProductoDetalleUIState.Success -> {
                            ProductoDetalleScreen(
                                producto = state.producto,
                                onClose = { finish() },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoDetalleScreen(
    producto: Producto,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tallaSeleccionada by remember { mutableStateOf("38") }
    var colorSeleccionado by remember { mutableStateOf("Negro") }

    val isInCart = CartManager.isSelected(producto.nombre)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        com.example.pa1android.components.MyTopAppBar(titulo = producto.nombre)

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
                model = getImagenURL(producto.imagenchica),
                contentDescription = producto.nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = painterResource(id = com.example.pa1android.R.drawable.img_zapato),
                placeholder = painterResource(id = com.example.pa1android.R.drawable.img_zapato)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "S/ ${String.format(Locale.getDefault(), "%.2f", producto.precio)}",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = producto.descripcion,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Selecciona tu Talla",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val listaTallas = producto.tallas ?: listOf("36", "37", "38", "39", "40")
            listaTallas.forEach { talla ->
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

        Text(
            text = "Color",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
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

        Spacer(modifier = Modifier.height(40.dp))

        Button( 
            onClick = {
                CartManager.toggleProduct(producto.nombre, producto.precio.toInt())
                onClose() 
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
