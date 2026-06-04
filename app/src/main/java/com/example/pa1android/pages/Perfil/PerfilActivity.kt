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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.pa1android.MainActivity
import com.example.pa1android.R
import com.example.pa1android.TerminosActivity
import com.example.pa1android.components.*
import com.example.pa1android.data.local.CartManager
import com.example.pa1android.data.local.UserStore
import com.example.pa1android.pages.compras.ComprasActivity
import com.example.pa1android.pages.tienda.TiendaActivity
import com.example.pa1android.ui.theme.ui.theme.PA1AndroidTheme
import com.example.pa1android.utils.clienteActivo
import com.example.pa1android.models.Cliente
import com.example.pa1android.utils.clienteActivo
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
                }, onLogout = {
                    lifecycleScope.launch {
                        val userStore = UserStore(this@PerfilActivity)
                        userStore.guardarDatosUsuario("") // Borramos sesión en DataStore
                        clienteActivo = null // Limpiamos variable global
                        val intent = Intent(this@PerfilActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                })
            }
        }
    }
}

@Composable
fun PerfilScreen(onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent(
            targetState = clienteActivo != null,
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
                                onClick = { /* TODO: Apple Login */ }
                            )
                            LoginButton(
                                text = "Continue with Google",
                                iconRes = R.drawable.google_logo,
                                containerColor = Color(0xFF2C2C2C),
                                onClick = { /* TODO: Google Login */ }
                            )
                            
                            val localContext = LocalContext.current
                            val scope = rememberCoroutineScope()
                            LoginButton(
                                text = "Continue with Email",
                                imageVector = Icons.Default.Email,
                                containerColor = Color(0xFF2C2C2C),
                                onClick = { 
                                    scope.launch {
                                        val userStore = UserStore(localContext)
                                        val datosUsuario = userStore.leerDatosUsuario.first()
                                        if (datosUsuario != null && datosUsuario.isNotEmpty()) {
                                            try {
                                                clienteActivo = Gson().fromJson(datosUsuario, Array<Cliente>::class.java).first()
                                                val intent = Intent(localContext, PerfilActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                localContext.startActivity(intent)
                                            } catch (e: Exception) {
                                                val intent = Intent(localContext, com.example.pa1android.pages.Clientes.ClientesActivity::class.java)
                                                localContext.startActivity(intent)
                                            }
                                        } else {
                                            val intent = Intent(localContext, com.example.pa1android.pages.Clientes.ClientesActivity::class.java)
                                            localContext.startActivity(intent)
                                        }
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val localContextForOficinas = LocalContext.current
                    // Acceso a Oficinas para usuarios no logueados (para prueba rápida)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        shadowElevation = 2.dp
                    ) {
                        SettingRow(
                            icon = Icons.Default.Place,
                            title = "Nuestras Oficinas",
                            subtitle = "Ver sucursales sin iniciar sesión",
                            onClick = {
                                localContextForOficinas.startActivity(Intent(localContextForOficinas, com.example.pa1android.pages.oficinas.OficinasActivity::class.java))
                            }
                        )
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    com.example.pa1android.components.MyTopAppBar(titulo = "Mi Perfil")

                    // Información del Cliente (Nueva de ProyectoThor)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = clienteActivo?.nombres ?: "Usuario Sin Nombre",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            if (!clienteActivo?.empresa.isNullOrEmpty()) {
                                Text(text = "Empresa: ${clienteActivo?.empresa}", fontSize = 14.sp, color = Color.Gray)
                            }
                            if (!clienteActivo?.cargo.isNullOrEmpty()) {
                                Text(text = "Cargo: ${clienteActivo?.cargo}", fontSize = 14.sp, color = Color.Gray)
                            }
                            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(text = "${clienteActivo?.ciudad ?: ""}, ${clienteActivo?.pais ?: ""}", fontSize = 14.sp, color = Color.Gray)
                            }
                            if (!clienteActivo?.telefono.isNullOrEmpty()) {
                                Text(text = "Tel: ${clienteActivo?.telefono}", fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
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

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Column {
                            val localContext = LocalContext.current
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
                                subtitle = "${clienteActivo?.ciudad ?: "Sin definir"}, ${clienteActivo?.pais ?: ""}" 
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                color = Color.LightGray.copy(alpha = 0.4f)
                            )
                            SettingRow(
                                icon = Icons.Default.Place,
                                title = "Nuestras Oficinas",
                                subtitle = "Gestionar sucursales de la tienda",
                                onClick = {
                                    localContext.startActivity(Intent(localContext, com.example.pa1android.pages.oficinas.OficinasActivity::class.java))
                                }
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
                            .clickable { mostrarDialogoCerrarSesion = true }
                            .padding(vertical = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }

        if (mostrarDialogoCerrarSesion) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoCerrarSesion = false },
                title = { Text("Cerrar sesión") },
                text = { Text("¿Estás seguro que desea cerrar sesión?") },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarDialogoCerrarSesion = false
                        onLogout()
                    }) {
                        Text("Sí", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoCerrarSesion = false }) {
                        Text("No", color = Color.Black)
                    }
                }
            )
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            FloatingNavBar(currentScreen = "Profile", onNavigate = onNavigate)
        }
    }
}
