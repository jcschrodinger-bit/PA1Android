package com.example.pa1android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FloatingNavBar(currentScreen: String, onNavigate: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(35.dp),
        color = Color.White.copy(alpha = 0.85f), // Efecto Cristal
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                Triple("Home", Icons.Outlined.Home, "Home"),
                Triple("Productos", Icons.Outlined.ShoppingBag, "Productos"),
                Triple("Compras", Icons.Outlined.ShoppingCart, "Compras"),
                Triple("Profile", Icons.Outlined.Person, "Profile")
            )

            items.forEach { (label, icon, target) ->
                val isSelected = currentScreen == target
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigate(target) }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(28.dp),
                        tint = if (isSelected) Color.Black else Color.Gray
                    )
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.Black else Color.Gray
                    )
                }
            }
        }
    }
}
