package com.example.pa1android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.ui.components.PrimaryButton
import com.example.pa1android.ui.theme.PA1AndroidTheme

class CheckoutSuccessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                CheckoutSuccessScreen(onBackToStore = {
                    // 1. Vaciamos el carrito de manera persistente local
                    CartManager.clearCart()

                    // 2. Redirigimos a la MainActivity limpiando el historial para no volver a esta pantalla al dar atrás
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    finish()

                    // Transición requerida por rúbrica
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                })
            }
        }
    }
}

@Composable
fun CheckoutSuccessScreen(onBackToStore: () -> Unit) {
    // Escala animada emulando el comportamiento .spring de tu Xcode
    val scaleAnim = remember { Animatable(0f) }

    // Al aparecer en pantalla, se dispara la física de rebote de muelle (dampingFraction: 0.6)
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = 0.6f, // Coeficiente de rebote idéntico a iOS
                stiffness = 400f
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fondo LightGray de la rúbrica
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Contenedor circular verde translúcido
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scaleAnim.value) // Inyección de la física de hardware
                .background(Color(0xFFE8F5E9), CircleShape), // Green con opacidad del 10%
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Éxito",
                tint = Color(0xFF4CAF50), // Verde esmeralda de confirmación
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Pedido Confirmado!",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tu calzado exclusivo de Elegance está siendo preparado para el envío.",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Reutilizamos nuestro componente refactorizado del Bloque Core
        PrimaryButton(title = "Volver a la Tienda") {
            onBackToStore()
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}