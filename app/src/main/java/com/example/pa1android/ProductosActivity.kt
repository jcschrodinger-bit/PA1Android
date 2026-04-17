package com.example.pa1android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pa1android.ui.theme.PA1AndroidTheme

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            startActivity(Intent(this@ProductosActivity, ComprasActivity::class.java))
                        }) {
                            Text("Ir al Carrito", modifier = Modifier.padding(8.dp))
                        }
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.title_activity_productos),
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Grilla con 9 imágenes de productos
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(9) { // Genera 9 elementos
                                Card {
                                    Image(
                                        painter = painterResource(R.drawable.img_zapato), // Agrega tu imagen
                                        contentDescription = stringResource(R.string.desc_zapato),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.height(120.dp).fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}