package com.example.pa1android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.ui.theme.PA1AndroidTheme

class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                PerfilScreen(onNavigate = { target ->
                    when(target) {
                        "Home" -> startActivity(Intent(this, MainActivity::class.java))
                        "Productos" -> startActivity(Intent(this, ProductosActivity::class.java))
                        "Compras" -> startActivity(Intent(this, ComprasActivity::class.java))
                        "Profile" -> {} // Ya estamos aquí
                    }
                })
            }
        }
    }
}

@Composable
fun PerfilScreen(onNavigate: (String) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "EleganceAnim")
    val translateY by infiniteTransition.animateFloat(
        initialValue = -15f, targetValue = 15f,
        animationSpec = infiniteRepeatable(animation = tween(2500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse), label = ""
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2500, easing = LinearOutSlowInEasing), repeatMode = RepeatMode.Reverse), label = ""
    )

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7))) {
        // Parte Superior con Animación (Igual que MainActivity)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Elegance",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(bottom = 300.dp) // Subimos el texto más para dejar espacio
                    .graphicsLayer {
                        translationY = translateY
                        this.alpha = alpha
                    }
            )
        }

        // Parte Inferior (Caja de Login)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 48.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginButton(
                        text = "Continue with Apple",
                        iconRes = R.drawable.apple_logo,
                        containerColor = Color(0xFF2C2C2C)
                    )
                    LoginButton(
                        text = "Continue with Google",
                        iconRes = R.drawable.google_logo,
                        containerColor = Color(0xFF2C2C2C)
                    )
                    LoginButton(
                        text = "Continue with Email",
                        imageVector = Icons.Default.Email,
                        containerColor = Color(0xFF2C2C2C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "By tapping Continue, you agree to our",
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
            val context = androidx.compose.ui.platform.LocalContext.current
            Text(
                text = "Terms",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        context.startActivity(Intent(context, TerminosActivity::class.java))
                    }
            )
            
            Spacer(modifier = Modifier.height(100.dp)) // Espacio para la NavBar
        }

        // Barra de Navegación
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            FloatingNavBar(currentScreen = "Profile", onNavigate = onNavigate)
        }
    }
}

@Composable
fun LoginButton(
    text: String,
    iconRes: Int? = null,
    imageVector: ImageVector? = null,
    containerColor: Color
) {
    Button(
        onClick = { /* Login Logic */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (iconRes != null) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
