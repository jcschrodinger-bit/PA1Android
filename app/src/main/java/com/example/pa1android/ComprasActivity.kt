package com.example.pa1android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pa1android.R
import com.example.pa1android.ui.theme.PA1AndroidTheme

class ComprasActivity : ComponentActivity() {
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
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = stringResource(R.string.desc_carrito),
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.title_activity_compras),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.img_compra), // Agrega tu imagen
                        contentDescription = stringResource(R.string.desc_compra_img),
                        modifier = Modifier.size(200.dp).padding(bottom = 32.dp)
                    )

                    Button(onClick = { /* Lógica de pago a futuro */ }) {
                        Text(text = stringResource(R.string.btn_pagar))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { finish() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                        Text(text = "Seguir Comprando")
                    }
                }
            }
        }
    }
}