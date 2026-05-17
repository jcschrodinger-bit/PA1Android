package com.example.pa1android

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.auth.TerminosActivity
import com.example.pa1android.ui.components.FloatingNavBar
import com.example.pa1android.ui.components.LoginButton
import com.example.pa1android.ui.components.SettingRow
import com.example.pa1android.ui.components.StatCard
import com.example.pa1android.ui.theme.PA1AndroidTheme

class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                PerfilScreen(onNavigate = { target ->
                    when (target) {
                        "Home" -> startActivity(Intent(this, MainActivity::class.java))
                        "Productos" -> startActivity(Intent(this, ProductosActivity::class.java))
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
    var startAnimation by remember { mutableStateOf(false) }

    // Animación infinita para el logotipo de Elegance en el Login
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

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Transición animada asimétrica controlada por el estado de sesión (.transition en Swift)
        AnimatedContent(
            targetState = isLoggedIn,
            transitionSpec = {
                if (targetState) {
                    // Entrada lateral desde la derecha (Slide In / Fade In)
                    slideInHorizontally { width -> width } + fadeIn() togetherWith fadeOut()
                } else {
                    fadeIn() togetherWith fadeOut()
                }
            },
            label = "ProfileStateTransition"
        ) { loggedIn ->
            if (!loggedIn) {
                // ==========================================
                // INTERFAZ DE LOGIN (LoginView)
                // ==========================================
                Column(
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

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(32.dp),
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            LoginButton(
                                text = "Continue with Apple",
                                iconRes = R.drawable.apple_logo,
                                containerColor = Color(0xFF2C2C2C),
                                onClick = { isLoggedIn = true }
                            )
                            LoginButton(
                                text = "Continue with Google",
                                iconRes = R.drawable.google_logo,
                                containerColor = Color(0xFF2C2C2C),
                                onClick = { isLoggedIn = true }
                            )
                            LoginButton(
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

                    val context = androidx.compose.ui.platform.LocalContext.current
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
                // ==========================================
                // VISTA DE PERFIL REAL (perfilRealView)
                // ==========================================
                val nombresProductos = CartManager.getProductsList().map { it.first }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Encabezado: Saludo y Foto Circular (.horizontal, 24)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 64.dp, bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Bienvenido de nuevo,",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Usuario Premium",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif, // Tipografía Serif obligatoria
                                color = Color.Black
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Foto de perfil",
                            tint = Color.Black.copy(alpha = 0.15f),
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    // Bloque Estadístico (StatCard de compras y favoritos)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Formateo con dos dígitos (%02d) idéntico a iOS
                        val totalCount = String.format("%02d", CartManager.getItemsCount())
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

                    // Título de Sección: Mi Inventario
                    Text(
                        text = "Mi Inventario",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 15.dp)
                    )

                    // Control Adaptativo del Armario Vacío o Lleno
                    if (nombresProductos.isEmpty()) {
                        // EmptyStateView Espejo
                        Column(
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
                        // Cuadrícula de 2 columnas de Inventario (LazyVGrid Espejo)
                        // Para usar una grilla dentro de un ScrollView vertical general sin romper los hilos,
                        // calculamos su altura o usamos trozos de filas mapeadas.
                        val chunks = nombresProductos.chunked(2)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            chunks.forEach { fila ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    fila.forEach { nombre ->
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

                    // Bloque Unificado de Configuraciones (Tarjeta encapsulada cornerRadius: 24)
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
                                subtitle = "Puente Piedra, Lima" // Tus datos de zona localizados
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón para Cerrar Sesión interactivo
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

        // Barra de navegación flotante global
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            FloatingNavBar(currentScreen = "Profile", onNavigate = onNavigate)
        }
    }
}

@Composable
fun InventoryCard(nombre: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag, // Homólogo elegante de calzado/adquisición
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.05f),
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = nombre,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 5.dp)
        )
        Text(
            text = "Adquirido",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}