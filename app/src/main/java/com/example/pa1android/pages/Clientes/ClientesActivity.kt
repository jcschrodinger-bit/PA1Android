package com.example.pa1android.pages.Clientes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.example.pa1android.R
import com.example.pa1android.components.MyTopAppBar
import com.example.pa1android.data.local.UserStore
import com.example.pa1android.models.Cliente
import com.example.pa1android.pages.Perfil.PerfilActivity
import com.example.pa1android.ui.theme.ui.theme.PA1AndroidTheme
import com.example.pa1android.ui.theme.ui.theme.dimens
import com.example.pa1android.utils.clienteActivo
import kotlinx.coroutines.launch

class ClientesActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[ClientesViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            PA1AndroidTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MyTopAppBar("Área de clientes")
                    }) { innerPadding ->
                    val uiState by viewModel.uiState.collectAsState()
                    var iniciarConsulta by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(MaterialTheme.dimens.large)
                    ) {
                        Image(
                            painterResource(R.drawable.img_portada), // Usando portada como placeholder
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .border(
                                    border = BorderStroke(4.dp, Color.Gray),
                                    shape = CircleShape
                                )
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = stringResource(R.string.iniciar_sesion),
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        OutlinedTextField(
                            label = { Text(text = stringResource(R.string.correo_telefono)) },
                            value = viewModel.correotelefono,
                            onValueChange = { viewModel.correotelefono = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            label = { Text(text = stringResource(R.string.clave)) },
                            value = viewModel.clave,
                            onValueChange = { viewModel.clave = it },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                            Checkbox(
                                checked = viewModel.estadoCheck,
                                onCheckedChange = {viewModel.estadoCheck = it}
                            )
                            Text(text = "Guardar inicio de sesión")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.fetchLogin()
                                iniciarConsulta = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.iniciar_sesion))
                        }
                        when (val state = uiState) {
                            is ClientesUIState.Loading -> {
                                if (iniciarConsulta) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                                }
                            }

                            is ClientesUIState.Error -> {
                                Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                            }

                            is ClientesUIState.Success -> {
                                evaluarResultado(state.resultado, viewModel)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun evaluarResultado(resultado: String, viewModel: ClientesViewModel) {
        val trimmedResultado = resultado.trim()
        when (trimmedResultado) {
            "-1" -> Toast.makeText(this, "La cuenta no existe", Toast.LENGTH_SHORT).show()
            "-2" -> Toast.makeText(this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show()
            else -> {
                if (trimmedResultado.isEmpty() || trimmedResultado.contains("Fatal error") || trimmedResultado.startsWith("<")) {
                    android.util.Log.e("LOGIN_ERROR", "Error del servidor o respuesta inesperada: $resultado")
                    Toast.makeText(this, "Error en el servidor", Toast.LENGTH_SHORT).show()
                    return
                }
                try {
                    val clientes = Gson().fromJson(trimmedResultado, Array<Cliente>::class.java)
                    if (clientes != null && clientes.isNotEmpty()) {
                        clienteActivo = clientes.first()
                        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, PerfilActivity::class.java))
                        if(viewModel.estadoCheck) {
                            lifecycleScope.launch {
                                val userStore = UserStore(this@ClientesActivity)
                                userStore.guardarDatosUsuario(trimmedResultado)
                            }
                        }
                    } else {
                        Toast.makeText(this, "No se encontraron datos de usuario", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    android.util.Log.e("LOGIN_ERROR", "Error al parsear JSON: $resultado", e)
                    Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
