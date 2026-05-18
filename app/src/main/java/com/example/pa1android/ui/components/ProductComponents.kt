package com.example.pa1android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pa1android.Network.APIConfig
import com.example.pa1android.Producto

@Composable
fun FilterRow(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Botas", "Zapatillas", "Tacos", "Sandalias")
    LazyRow( // Fila desplazable para los filtros
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filters) { filter -> // Repetir para cada filtro de la lista
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Black,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    labelColor = Color.Black
                ),
                border = null,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun ProductCard( // Tarjeta individual del producto
    producto: Producto,
    isInitiallyFavorite: Boolean = false,
    onFavoriteChanged: (Boolean) -> Unit = {},
    onClick: () -> Unit
) {
    var isFavorite by remember(isInitiallyFavorite) { mutableStateOf(isInitiallyFavorite) }

    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }) { // Contenedor vertical
        Box( // Contenedor para la imagen y el botón de favorito
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            AsyncImage(
                model = APIConfig.getImagenURL(producto.imagen ?: ""),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    onFavoriteChanged(isFavorite)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
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
            text = producto.nombre ?: "",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 1
        )

        val precioDouble = producto.precioBase ?: 0.0
        Text(
            text = "S/ ${String.format("%.2f", precioDouble)}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
