package com.example.pa1android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pa1android.ui.theme.PA1AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        text = stringResource(R.string.slogan),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Imagen de Portada
                    Image(
                        painter = painterResource(R.drawable.img_portada), // Necesitas agregar esta imagen
                        contentDescription = stringResource(R.string.desc_portada),
                        modifier = Modifier.size(250.dp).padding(bottom = 32.dp)
                    )

                    // Navegación
                    Button(onClick = { startActivity(Intent(this@MainActivity, ProductosActivity::class.java)) }) {
                        Text(text = stringResource(R.string.btn_ver_productos))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { startActivity(Intent(this@MainActivity, ContactoActivity::class.java)) }) {
                        Text(text = stringResource(R.string.btn_contacto))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { startActivity(Intent(this@MainActivity, TerminosActivity::class.java)) }) {
                        Text(text = stringResource(R.string.btn_terminos))
                    }
                }
            }
        }
    }
}