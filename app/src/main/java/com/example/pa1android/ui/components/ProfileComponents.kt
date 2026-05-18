package com.example.pa1android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileHeader() {
    Row( // Fila horizontal para el saludo y la foto
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 64.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { // Contenedor vertical para el texto
            Text(
                text = "Bienvenido de nuevo,",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Usuario Premium",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.Black
            )
        }
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Foto de perfil",
            tint = Color.Black.copy(alpha = 0.15f),
            modifier = Modifier.size(54.dp)
        )
    }
}

@Composable
fun InventoryCard(nombre: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) { // Contenedor vertical de la tarjeta de inventario
        Surface( // Superficie de la tarjeta
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Box(contentAlignment = Alignment.Center) { // Contenedor para centrar el icono
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.05f),
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = nombre,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 5.dp)
        )
        Text(
            text = "Adquirido",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}
