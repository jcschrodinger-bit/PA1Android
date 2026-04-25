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
import androidx.compose.ui.platform.LocalUriHandler
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
        enableEdgeToEdge() // optimiza el dibujo de la interfaz para que use todo el espacio de la pantalla
        setContent { // punto de entrada de compose: define la jerarquía de UI sin usar archivos XML
            PA1AndroidTheme {
                // remember y mutableStateOf gestionan el estado de navegación interna
                var showWelcomeContent by remember { mutableStateOf(false) }

                if (!showWelcomeContent) {// condicional que carga la pantalla de bienvenida basándose en el estado booleano
                    WelcomeAnimationScreen(onScreenClick = {
                        showWelcomeContent = true // dispara el cambio de estado para mostrar el contenido principal
                    })
                } else {
                    Box( // el Box actúa como contenedor raíz para superponer elementos del fondo y la barra de navegación
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        MainWelcomePage() // carga el módulo de contenido informativo y branding

                        // centra a la barra de navegación en la parte inferior central
                        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                            FloatingNavBar(currentScreen = "Home", onNavigate = { target ->
                                // gestión de navegación explícita mediante Intents según el destino seleccionado
                                when (target) {
                                    "Productos" -> startActivity(Intent(this@MainActivity, ProductosActivity::class.java))
                                    "Compras" -> startActivity(Intent(this@MainActivity, ComprasActivity::class.java))
                                    "Profile" -> startActivity(Intent(this@MainActivity, PerfilActivity::class.java))
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
    // Definición de animaciones infinitas para el logo: interpolación de posición (Y) y transparencia (Alpha)
    val infiniteTransition = rememberInfiniteTransition(label = "EleganceAnim")
    val translateY by infiniteTransition.animateFloat(
        initialValue = -15f, targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val alpha by infiniteTransition.animateFloat(
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
            .clickable { onScreenClick() }, // Captura el evento táctil para transicionar a la pantalla principal
        contentAlignment = Alignment.Center
    ) {
        // Uso de graphicsLayer para aplicar transformaciones de hardware sin afectar el layout general
        Text(
            text = "Elegance",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .graphicsLayer { translationY = translateY; this.alpha = alpha }
        )
    }
}

@Composable
fun MainWelcomePage() {
    val uriHandler = LocalUriHandler.current // Manejador para la apertura de enlaces externos (URLs)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // Renderizado de imagen promocional con recorte de bordes (clip) y ajuste de escala tipo 'Crop'
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
            text = stringResource(id = R.string.slogan), // Recuperación de strings desde el archivo de recursos (internacionalización)
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        // El weight(1f) distribuye el espacio restante, asegurando que el contenido siguiente se desplace al fondo
        Spacer(modifier = Modifier.weight(1f))

        // Implementación de enlace interactivo con apertura de navegador nativo
        Text(
            text = "Visítanos en https://jcmesia.alwaysdata.net/productos.php",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .clickable {
                    uriHandler.openUri("https://jcmesia.alwaysdata.net/productos.php")
                }
        )

        // Margen de seguridad inferior para evitar la superposición visual de la FloatingNavBar
        Spacer(modifier = Modifier.height(110.dp))
    }
}
