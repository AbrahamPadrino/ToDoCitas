package com.example.todocitas.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todocitas.components.CustomTextField
import com.example.todocitas.data.local.entities.Servicio
import com.example.todocitas.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoServicioView(
    navController: NavController,
    onBack: () -> Unit,
    onSaveService: (Servicio) -> Unit = {}
) {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Nuevo Servicio",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White // El diseño original lo muestra en color primario, pero en dark theme se ve mejor blanco.
                        )
                    }
                },
                actions = {
                    // Se añade un espaciador para centrar correctamente el título
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Campos de texto del formulario
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    CustomTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = "Nombre",
                        placeholder = "Ej. Corte de pelo",
                        singleLine = true
                    )
                    CustomTextField(
                        value = precio,
                        onValueChange = { precio = it },
                        label = "Precio",
                        placeholder = "0.00",
                        leadingIcon = { Text("€", color = TextSecondary, fontSize = 16.sp) },
                        keyboardType = KeyboardType.Number,
                        singleLine = true
                    )
                    CustomTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = "Descripción",
                        placeholder = "Describe brevemente el servicio...",
                        singleLine = false,
                        modifier = Modifier.height(120.dp) // Altura para el campo de texto multi-línea
                    )
                }

                // Espaciador para dejar sitio a los botones flotantes
                Spacer(modifier = Modifier.height(180.dp))
            }

            // Footer con botones y gradiente
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                BackgroundDark.copy(alpha = 0.8f),
                                BackgroundDark
                            )
                        )
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { /* Acción para guardar servicio */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardDark,
                            contentColor = TextSecondary
                        )
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

// --- Preview para Android Studio ---
@Preview(showBackground = true, backgroundColor = 0xFF101C22)
@Composable
fun NuevoServicioViewPreview() {
    ToDoCitasTheme {
        NuevoServicioView(
            navController = NavController(LocalContext.current),
            onBack = {}
        )
    }
}