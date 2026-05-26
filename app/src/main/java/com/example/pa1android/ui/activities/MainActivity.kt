package com.example.pa1android.ui.activities

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pa1android.R
import com.example.pa1android.data.local.CartManager
import com.example.pa1android.ui.components.FloatingNavBar
import com.example.pa1android.ui.theme.PA1AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CartManager.init(applicationContext) 
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                Box( // Contenedor para poner cosas una encima de otra
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    MainWelcomePage() // Se despliega inmediatamente con su banner e información

                    Box(modifier = Modifier.align(Alignment.BottomCenter)) { // Contenedor para la barra inferior
                        FloatingNavBar(currentScreen = "Home", onNavigate = { target ->
                            when (target) {
                                "Productos" -> {
                                    startActivity(Intent(this@MainActivity, CategoriasActivity::class.java))
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right) // Transición 2
                                }
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

@Composable
fun MainWelcomePage() {
    val uriHandler = LocalUriHandler.current 
    Column( // Contenedor para poner cosas una debajo de otra
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (resto del código igual pero con el package correcto)
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

        Spacer(modifier = Modifier.weight(1f))

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

        Spacer(modifier = Modifier.height(110.dp))
    }
}
