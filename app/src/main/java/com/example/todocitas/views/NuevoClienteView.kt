package com.example.todocitas.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todocitas.components.CustomTextField
import com.example.todocitas.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoClienteView(
    navController: NavController,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Nuevo Cliente",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Sección Añadir Foto
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { /* Acción para seleccionar foto */ },
                contentAlignment = Alignment.Center
            ) {
                // Placeholder de la imagen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFFE0CFC2)) // Color beige claro del placeholder
                        .border(4.dp, CardDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Botón de editar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar foto",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Añadir foto",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                "Toca para cambiar",
                color = Primary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                    placeholder = "Ej. Juan"
                )
                CustomTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = "Apellido",
                    placeholder = "Ej. Pérez"
                )
                CustomTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = "Correo",
                    placeholder = "ejemplo@correo.com",
                    keyboardType = KeyboardType.Email
                )
                CustomTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = "Teléfono",
                    placeholder = "+34 600 000 000",
                    keyboardType = KeyboardType.Phone
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botones de acción
            Button(
                onClick = { /* Acción para guardar cliente */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Cliente", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onBack) {
                Text(
                    "Cancelar",
                    color = TextSecondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}



// --- Preview para Android Studio ---
@Preview(showBackground = true, backgroundColor = 0xFF101C22)
@Composable
fun NuevoClienteViewPreview() {
    ToDoCitasTheme {
        NuevoClienteView(
            navController = rememberNavController(),
            onBack = {}
        )
    }
}

