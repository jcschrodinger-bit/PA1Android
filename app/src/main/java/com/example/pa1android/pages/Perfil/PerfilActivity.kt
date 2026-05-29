package com.example.pa1android.pages.Perfil

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.MainActivity
import com.example.pa1android.R
import com.example.pa1android.TerminosActivity
import com.example.pa1android.components.*
import com.example.pa1android.data.local.CartManager
import com.example.pa1android.pages.compras.ComprasActivity
import com.example.pa1android.pages.tienda.TiendaActivity
import com.example.pa1android.ui.theme.ui.theme.PA1AndroidTheme
import java.util.Locale

class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                PerfilScreen(onNavigate = { target ->
                    when (target) {
                        "Home" -> startActivity(Intent(this, MainActivity::class.java))
                        "Productos" -> startActivity(Intent(this, TiendaActivity::class.java))
                        "Compras" -> startActivity(Intent(this, ComprasActivity::class.java))
                        "Profile" -> {}
                    }
                })
            }
        }
    }
}

@Composable
fun PerfilScreen(onNavigate: (String) -> Unit) {
    var isLoggedIn by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "LoginAnim")
    val translateY by infiniteTransition.animateFloat(
        initialValue = -15f, targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box( // Contenedor para apilar cosas una encima de otra
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent( // Animación para cambiar de pantalla
            targetState = isLoggedIn,
            transitionSpec = {
                if (targetState) {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith fadeOut()
                } else {
                    fadeIn() togetherWith fadeOut()
                }
            },
            label = "ProfileStateTransition"
        ) { loggedIn ->
            if (!loggedIn) {
                Column( // Contenedor vertical
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(0.4f))

                    Text(
                        text = "Elegance",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.graphicsLayer {
                            translationY = translateY
                            this.alpha = alphaAnim
                        }
                    )

                    Spacer(modifier = Modifier.weight(0.6f))

                    Surface( // Superficie para el fondo blanco
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(32.dp),
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Column( // Contenedor vertical para los botones
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            LoginButton( // Botón para Apple
                                text = "Continue with Apple",
                                iconRes = R.drawable.apple_logo,
                                containerColor = Color(0xFF2C2C2C),
                                onClick = { isLoggedIn = true }
                            )
                            LoginButton( // Botón para Google
                                text = "Continue with Google",
                                iconRes = R.drawable.google_logo,
                                containerColor = Color(0xFF2C2C2C),
                                onClick = { isLoggedIn = true }
                            )
                            LoginButton( // Botón para Email
                                text = "Continue with Email",
                                imageVector = Icons.Default.Email,
                                containerColor = Color(0xFF2C2C2C),
                                onClick = { isLoggedIn = true }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Al tocar/pulsar continuar, aceptas nuestros",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    val context = LocalContext.current
                    Text(
                        text = "Términos",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .clickable {
                                context.startActivity(Intent(context, TerminosActivity::class.java))
                            }
                            .padding(4.dp)
                    )

                    Spacer(modifier = Modifier.height(120.dp))
                }
            } else {
                val nombresProductos = CartManager.getProductsList().map { it.first }

                Column( // Contenedor vertical con scroll
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    ProfileHeader()

                    Row( // Contenedor horizontal
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val totalCount = String.format(Locale.getDefault(), "%02d", CartManager.getItemsCount())
                        StatCard(
                            title = "Compras",
                            value = totalCount,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Favoritos",
                            value = "12",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "Mi Inventario",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 15.dp)
                    )

                    if (nombresProductos.isEmpty()) {
                        Column( // Contenedor vertical centrado
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Tu armario está esperando nuevas joyas.",
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        val chunks = nombresProductos.chunked(2)
                        Column( // Contenedor vertical para las filas de productos
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            chunks.forEach { fila -> // Repetir por cada fila de dos productos
                                Row( // Fila horizontal
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    fila.forEach { nombre -> // Repetir para cada producto de la fila
                                        InventoryCard(nombre = nombre, modifier = Modifier.weight(1f))
                                    }
                                    if (fila.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Column {
                            SettingRow(
                                icon = Icons.Default.CreditCard,
                                title = "Métodos de Pago",
                                subtitle = "Visa **** 4242"
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                color = Color.LightGray.copy(alpha = 0.4f)
                            )
                            SettingRow(
                                icon = Icons.Default.Place,
                                title = "Dirección",
                                subtitle = "Puente Piedra, Lima" 
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isLoggedIn = false }
                            .padding(vertical = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            FloatingNavBar(currentScreen = "Profile", onNavigate = onNavigate)
        }
    }
}
