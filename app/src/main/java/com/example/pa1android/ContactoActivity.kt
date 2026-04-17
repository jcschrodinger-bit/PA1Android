package com.example.pa1android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pa1android.R
import com.example.pa1android.ui.theme.PA1AndroidTheme

class ContactoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.title_activity_contacto),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                    )

                    // Imagen del Mapa
                    Image(
                        painter = painterResource(R.drawable.img_mapa), // Agrega tu imagen del mapa
                        contentDescription = stringResource(R.string.desc_mapa),
                        modifier = Modifier.fillMaxWidth().height(200.dp).padding(bottom = 24.dp)
                    )

                    // Información de contacto con Íconos
                    ContactRow(icon = Icons.Filled.LocationOn, text = stringResource(R.string.contacto_direccion))
                    ContactRow(icon = Icons.Filled.Phone, text = stringResource(R.string.contacto_telefono))
                    ContactRow(icon = Icons.Filled.Email, text = stringResource(R.string.contacto_email))
                    ContactRow(icon = Icons.Filled.Share, text = "@ZapateriaElegance")

                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { finish() }) {
                        Text(text = "Volver")
                    }
                }
            }
        }
    }
}

// Función Composable auxiliar para no repetir código en las filas de contacto
@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}