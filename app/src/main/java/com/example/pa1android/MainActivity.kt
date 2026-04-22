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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.ui.theme.PA1AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                var showWelcomeContent by remember { mutableStateOf(false) }

                if (!showWelcomeContent) {
                    WelcomeAnimationScreen(onScreenClick = {
                        showWelcomeContent = true
                    })
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                        MainWelcomePage()
                        
                        // Barra de Navegación Estilo Cápsula Fixed y Transparente
                        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                            FloatingNavBar(currentScreen = "Home", onNavigate = { target ->
                                when(target) {
                                    "Productos" -> startActivity(Intent(this@MainActivity, ProductosActivity::class.java))
                                    "Compras" -> startActivity(Intent(this@MainActivity, ComprasActivity::class.java))
                                    "Contacto" -> startActivity(Intent(this@MainActivity, ContactoActivity::class.java))
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeAnimationScreen(onScreenClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "EleganceAnim")
    val translateY by infiniteTransition.animateFloat(
        initialValue = -15f, targetValue = 15f,
        animationSpec = infiniteRepeatable(animation = tween(2500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse), label = ""
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2500, easing = LinearOutSlowInEasing), repeatMode = RepeatMode.Reverse), label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).clickable { onScreenClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Elegance",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.graphicsLayer { translationY = translateY; this.alpha = alpha }
        )
    }
}

@Composable
fun MainWelcomePage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Image(
            painter = painterResource(id = R.drawable.img_bienvenida),
            contentDescription = "Tienda Elegance",
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(420.dp)
                .clip(RoundedCornerShape(32.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Elegance",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.slogan),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}

