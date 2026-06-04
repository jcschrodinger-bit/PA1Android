package com.example.pa1android.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pa1android.ui.theme.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(titulo: String) {
    val context = LocalContext.current
    val colorPrimario = Color.Black // Usando el color primario de PA1Android
    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            Surface(
                onClick = { (context as? ComponentActivity)?.finish() },
                shape = CircleShape,
                color = colorPrimario.copy(0.1f),
                modifier = Modifier.padding(start = MaterialTheme.dimens.small)
            ) {
                Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = colorPrimario
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun FloatingNavBar(currentScreen: String, onNavigate: (String) -> Unit = {}) {
    Surface(
        modifier = Modifier
            .padding(bottom = 32.dp, start = 20.dp, end = 20.dp) 
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(35.dp), 
        color = Color.White.copy(alpha = 0.85f), 
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                label = "Inicio",
                activeIcon = Icons.Filled.Home,
                inactiveIcon = Icons.Outlined.Home,
                isSelected = currentScreen == "Home",
                onClick = { onNavigate("Home") }
            )

            NavBarItem(
                label = "Productos",
                activeIcon = Icons.Filled.ShoppingBag,
                inactiveIcon = Icons.Outlined.ShoppingBag,
                isSelected = currentScreen == "Productos",
                onClick = { onNavigate("Productos") }
            )

            NavBarItem(
                label = "Compras",
                activeIcon = Icons.Filled.ShoppingCart,
                inactiveIcon = Icons.Outlined.ShoppingCart,
                isSelected = currentScreen == "Compras",
                onClick = { onNavigate("Compras") }
            )

            NavBarItem(
                label = "Perfil",
                activeIcon = Icons.Filled.Person,
                inactiveIcon = Icons.Outlined.Person,
                isSelected = currentScreen == "Profile",
                onClick = { onNavigate("Profile") }
            )
        }
    }
}

@Composable
fun RowScope.NavBarItem(
    label: String,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .weight(1f) 
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null 
            ) { onClick() }
    ) {
        Icon(
            imageVector = if (isSelected) activeIcon else inactiveIcon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = if (isSelected) Color.Black else Color.Gray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.Black else Color.Gray,
            maxLines = 1, 
            overflow = TextOverflow.Ellipsis
        )
    }
}
