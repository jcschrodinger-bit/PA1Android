package com.example.pa1android.pages.oficinas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.pa1android.components.MyTopAppBar
import com.example.pa1android.ui.theme.ui.theme.PA1AndroidTheme

class OficinasActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[OficinasViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                var mostrarBottomSheet by remember { mutableStateOf(false) }
                var estadoActualizar by remember { mutableStateOf(false) }
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MyTopAppBar("Nuestras Oficinas")
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                viewModel.nombre = ""
                                viewModel.ciudad = ""
                                estadoActualizar = false
                                mostrarBottomSheet = true
                            },
                            containerColor = Color.Black,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nueva Oficina")
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        when (val state = uiState) {
                            is OficinasUIState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = Color.Black
                                )
                            }
                            is OficinasUIState.Success -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    items(state.oficinas, key = { it.idoficina }) { oficina ->
                                        Box(modifier = Modifier.clickable {
                                            viewModel.idoficina = oficina.idoficina.toString()
                                            viewModel.nombre = oficina.nombre
                                            viewModel.ciudad = oficina.ciudad
                                            estadoActualizar = true
                                            mostrarBottomSheet = true
                                        }) {
                                            FilaOficina(oficina)
                                        }
                                    }
                                }
                            }
                            is OficinasUIState.Error -> {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = state.message, color = Color.Red)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { viewModel.fetchOficinas() },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                    ) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }

                        if (mostrarBottomSheet) {
                            ModalBottomSheet(
                                onDismissRequest = { mostrarBottomSheet = false },
                                containerColor = Color.White,
                                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp)
                                        .navigationBarsPadding()
                                ) {
                                    Text(
                                        text = if (estadoActualizar) "Editar Oficina" else "Nueva Oficina",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 24.dp)
                                    )
                                    
                                    OutlinedTextField(
                                        value = viewModel.nombre,
                                        onValueChange = { viewModel.nombre = it },
                                        label = { Text("Nombre de la oficina") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    OutlinedTextField(
                                        value = viewModel.ciudad,
                                        onValueChange = { viewModel.ciudad = it },
                                        label = { Text("Ciudad") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(32.dp))
                                    
                                    Button(
                                        onClick = {
                                            if (estadoActualizar) {
                                                viewModel.updateOficina()
                                            } else {
                                                viewModel.insertOficina()
                                            }
                                            mostrarBottomSheet = false
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(56.dp),
                                        shape = RoundedCornerShape(28.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                    ) {
                                        Text(
                                            text = if (estadoActualizar) "Actualizar" else "Guardar",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
