package com.example.pa1android.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.data.model.Terminos
import com.example.pa1android.data.network.RetrofitClient
import com.example.pa1android.ui.theme.PA1AndroidTheme
import kotlinx.coroutines.launch

class TerminosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                TerminosScreen(onClose = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(onClose: () -> Unit) {
    var datosTerminos by remember { mutableStateOf<Terminos?>(null) }
    var cargandoTexto by remember { mutableStateOf(true) }
    var errorConexion by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                cargandoTexto = true
                val resultado = RetrofitClient.apiService.obtenerTerminos()
                datosTerminos = resultado
                errorConexion = false
            } catch (e: Exception) {
                e.printStackTrace()
                errorConexion = true
            } finally {
                cargandoTexto = false
            }
        }
    }

    Scaffold( // Estructura básica de la pantalla con barra superior
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background, 
        topBar = {
            CenterAlignedTopAppBar( // Barra superior centrada
                title = {
                    Text(
                        text = datosTerminos?.titulo ?: "Políticas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Box( // Contenedor para centrar el contenido (carga o texto)
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (cargandoTexto) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { // Contenedor vertical
                    CircularProgressIndicator(color = Color.Black)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Descargando políticas...", color = Color.Gray, fontSize = 14.sp)
                }
            } else if (errorConexion) {
                Text(
                    text = "No se pudieron cargar los términos.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            } else {
                datosTerminos?.let { terminos ->
                    Column( // Contenedor vertical con scroll para los textos
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Text(
                            text = terminos.fechaActualizacion,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )

                        TerminosSeccion(titulo = terminos.seccion1Titulo, texto = terminos.seccion1Texto)
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                        TerminosSeccion(titulo = terminos.seccion2Titulo, texto = terminos.seccion2Texto)
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                        TerminosSeccion(titulo = terminos.seccion3Titulo, texto = terminos.seccion3Texto)

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TerminosSeccion(titulo: String, texto: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Contenedor vertical para cada sección
        Text(
            text = titulo,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif, 
            color = Color.Black
        )
        Text(
            text = texto,
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.7f),
            lineHeight = 22.sp,
            textAlign = TextAlign.Justify
        )
    }
}
