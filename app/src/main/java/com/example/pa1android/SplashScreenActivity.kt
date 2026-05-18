package com.example.pa1android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.ui.theme.PA1AndroidTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                SplashScreenContent(onTimeout = {
                    // Viaja a la MainActivity con la transición fluida requerida
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Destruye el Splash para que no se pueda regresar con el botón atrás

                    // Requerimiento 2.1.4: Transición animada fluida entre Activities (Fade In / Fade Out)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                })
            }
        }
    }
}

@Composable
fun SplashScreenContent(onTimeout: () -> Unit) {
    // Estado inicial falso para disparar las animaciones al entrar a la pantalla
    var startAnimation by remember { mutableStateOf(false) }

    // OBJETO ANIMADO 1: Escala/Zoom del Ícono del proyecto (La "E")
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "ScaleE"
    )

    // OBJETO ANIMADO 2: Desvanecimiento / Opacidad (Alpha) del Eslogan Premium
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1800, easing = LinearOutSlowInEasing),
        label = "AlphaText"
    )

    // OBJETO ANIMADO 3: Expansión de Ancho Horizontal de la línea de carga de Diseño
    val widthAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutLinearInEasing),
        label = "WidthLine"
    )

    // Lanzador del ciclo de vida (Temporizador automatizado de 3 segundos)
    LaunchedEffect(key1 = true) {
        startAnimation = true // Dispara los 3 objetos visuales al mismo tiempo
        delay(3000)           // Duración exacta antes del salto de pantalla
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)), // Fondo claro minimalista de la rúbrica
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animación 1: Icono Personalizado del Proyecto (La "E" elegante en negrita)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scaleAnim)
                    .background(Color.White, RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "E",
                    fontSize = 64.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF333333)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // [Animación 2: Subtítulo de branding con desvanecimiento asíncrono
            Text(
                text = "E L E G A N C E",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Light,
                color = Color(0xFF333333),
                modifier = Modifier.alpha(alphaAnim)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Animación 3]: Línea de corte arquitectónico que se expande horizontalmente
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.25f * widthAnim) // Crece dinámicamente según la animación
                    .height(2.dp)
                    .background(Color(0xFF333333))
            )
        }
    }
}