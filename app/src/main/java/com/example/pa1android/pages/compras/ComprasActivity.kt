package com.example.pa1android.pages.compras

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.CheckoutSuccessActivity
import com.example.pa1android.MainActivity
import com.example.pa1android.TerminosActivity
import com.example.pa1android.components.CartItemRow
import com.example.pa1android.components.PaymentSummaryPanel
import com.example.pa1android.data.local.CartManager
import com.example.pa1android.ui.theme.ui.theme.PA1AndroidTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ComprasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                CartScreen(onClose = { finish() })
            }
        }
    }
}

@Composable
fun CartScreen(onClose: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var procesandoPago by remember { mutableStateOf(false) }
    val productosComprados = CartManager.getProductsList()
    val total = CartManager.getTotal()

    val seaGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF01121B),
            Color(0xFF042940),
            Color(0xFF01121B)
        )
    )

    Box( // Contenedor para apilar cosas una encima de otra
        modifier = Modifier
            .fillMaxSize()
            .background(seaGradient)
    ) {
        Column( // Contenedor vertical
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Row( // Contenedor horizontal
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mi Carrito",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Salir",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "Detalle de tus productos exclusivos",
                color = Color.Gray,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            if (productosComprados.isEmpty()) {
                Box( // Contenedor centrado para cuando el carrito está vacío
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.5f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn( // Lista desplazable de productos
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(productosComprados) { producto -> // Repetir para cada producto de la lista
                        CartItemRow(nombre = producto.first, precio = producto.second)
                    }
                }
            }
        }

        Column( // Contenedor vertical inferior
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(MaterialTheme.colorScheme.background) 
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaymentSummaryPanel(total = total)

            Spacer(modifier = Modifier.height(20.dp))

            Button( // Botón para pagar
                onClick = {
                    coroutineScope.launch {
                        procesandoPago = true 
                        delay(2000)          
                        procesandoPago = false

                        val intent = Intent(context, CheckoutSuccessActivity::class.java)
                        context.startActivity(intent)
                        (context as? android.app.Activity)?.overridePendingTransition(
                            android.R.anim.fade_in, android.R.anim.fade_out
                        )
                    }
                },
                enabled = !procesandoPago && productosComprados.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF262626),
                    disabledContainerColor = Color(0xFF262626).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row( // Contenedor horizontal para el contenido del botón
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (procesandoPago) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp),
                            strokeWidth = 2.dp
                        )
                        Text(text = "Procesando...", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text(text = "Pagar Ahora", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Términos y Condiciones",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable {
                        context.startActivity(Intent(context, TerminosActivity::class.java))
                    }
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
