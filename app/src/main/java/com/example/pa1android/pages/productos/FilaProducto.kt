package com.example.pa1android.pages.productos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pa1android.models.Producto
import com.example.pa1android.utils.getImagenURL
import java.util.Locale

@Composable
fun FilaProducto(
    itemProducto: Producto,
    isInitiallyFavorite: Boolean = false,
    onFavoriteChanged: (Boolean) -> Unit = {},
    onClick: () -> Unit
) {
    var isFavorite by remember(isInitiallyFavorite) { mutableStateOf(isInitiallyFavorite) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(8.dp) 
    ) {
        val rutaImagen = getImagenURL(itemProducto.imagenchica)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White),
            contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                model = rutaImagen,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(6.dp).clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = com.example.pa1android.R.drawable.img_zapato),
                placeholder = painterResource(id = com.example.pa1android.R.drawable.img_zapato)
            )

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    onFavoriteChanged(isFavorite)
                },
                modifier = Modifier
                    .padding(12.dp)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = itemProducto.nombre,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Start,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        val precioFinal = itemProducto.precio

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "S/ ${String.format(Locale.getDefault(), "%.2f", precioFinal)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
