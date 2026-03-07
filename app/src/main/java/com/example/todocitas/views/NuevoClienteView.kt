package com.example.todocitas.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File // Import para manejar archivos
import java.io.FileOutputStream // Import para escribir archivos
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.todocitas.components.CustomTextField
import com.example.todocitas.ui.theme.*
import com.example.todocitas.viewmodels.ClientesViewModel
import com.example.todocitas.data.local.entities.Cliente
import androidx.core.net.toUri
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoClienteView(
    onBack: () -> Unit,
    navController: NavController,
    openDrawer: () -> Unit,
    clienteId: Int,
    clientesViewModel: ClientesViewModel,
    onSaveComplete: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(key1 = clienteId) {
        if (clienteId != -1) {
            // Si estamos en modo edición, pedimos al ViewModel que cargue los datos del cliente.
            val cliente = clientesViewModel.getClienteById(clienteId)
            cliente?.let {
                // Rellenamos los estados del formulario con los datos cargados.
                nombre = it.nombre
                apellido = it.apellido
                correo = it.correo
                telefono = it.telefono
                imagenUri = it.imagenUri?.toUri()
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { tempUri: Uri? ->
        // Este bloque se ejecuta cuando el usuario selecciona una imagen (o cancela).
        // 'tempUri' es la URI temporal con permiso de corta duración.
        // Si `uri` no es nulo, lo guardamos en nuestro estado.
        tempUri?.let {
            // --- 2. LÓGICA PARA COPIAR LA IMAGEN Y OBTENER UNA URI PERSISTENTE ---
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            try {
                // Usamos un InputStream para leer los datos de la URI temporal
                val inputStream = context.contentResolver.openInputStream(it)
                // Usamos un FileOutputStream para escribir los datos en nuestro nuevo archivo
                val outputStream = FileOutputStream(file)
                // Copiamos los bytes
                inputStream?.copyTo(outputStream)

                inputStream?.close()
                outputStream.close()

                // 3. ¡LA PARTE CLAVE! Actualizamos nuestro estado con la URI de NUESTRO archivo.
                // Esta URI es persistente y siempre será accesible para nuestra app.
                imagenUri = Uri.fromFile(file)

            } catch (e: Exception) {
                // Manejar la excepción (ej: mostrar un Toast de error)
                e.printStackTrace()
            }
            //imagenUri = it
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Nuevo Cliente",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "menu",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top =paddingValues.calculateTopPadding())
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Sección Añadir Foto
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imagenUri != null) {
                        // Usamos Coil para cargar la imagen desde la Uri.
                        AsyncImage(
                            model = imagenUri,
                            contentDescription = "Foto de perfil seleccionada",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(4.dp, CardDark, CircleShape),
                            contentScale = ContentScale.Crop // Asegura que la imagen llene el círculo
                        )
                    } else {
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

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                // Campos de texto del formulario
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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

                Spacer(modifier = Modifier.height(160.dp))

            }
            // FOOTER
            Box(
                modifier = Modifier
                    //BottomCenter
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    //.navigationBarsPadding() // Respeta la barra de sistema
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                BackgroundDark.copy(alpha = 0.95f),
                                BackgroundDark
                            )
                    )
                    )
                    .padding(horizontal = 24.dp, vertical = 58.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Botones de acción
                    Button(
                        onClick = {
                            if (clienteId == -1) {
                                // Modo Creación
                                clientesViewModel.agregarCliente(
                                    Cliente(
                                        nombre = nombre,
                                        apellido = apellido,
                                        correo = correo,
                                        telefono = telefono,
                                        imagenUri = imagenUri?.toString()
                                    )
                                )
                            } else {
                                // Modo Edición
                                clientesViewModel.updateCliente(
                                    Cliente(
                                        id = clienteId, // ¡Mantenemos el ID original!
                                        nombre = nombre,
                                        apellido = apellido,
                                        correo = correo,
                                        telefono = telefono,
                                        imagenUri = imagenUri?.toString()
                                    )
                                )
                            }

                            mostrarDialogo = true
                        },
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
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (clienteId == -1) "Guardar Cliente" else "Actualizar Cliente",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
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
                        Text(
                            "Cancelar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                }

            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                // Se llama cuando el usuario presiona fuera del diálogo.
                // También cerramos la pantalla en este caso.
                onBack()
            },
            title = {
                Text(
                    text = "¡Finalizado!",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Text(
                    if (clienteId == -1) "Cliente guardado con éxito" else "Cliente actualizado con éxito",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        onSaveComplete() // Usamos el callback para navegar hacia atrás
                    }
                ) {
                    Text("Confirmar", color = Primary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = CardDark,
            titleContentColor = Color.White,
            textContentColor = TextSecondary
        )
    }
}

// --- Preview para Android Studio ---
@Preview(showBackground = true, backgroundColor = 0xFF101C22)
@Composable
fun NuevoClienteViewPreview() {
    ToDoCitasTheme {
        NuevoClienteView(
            openDrawer = {},
            navController = NavController(LocalContext.current),
            onBack = {},
            clienteId = TODO(),
            clientesViewModel = TODO(),
            onSaveComplete = {}
        )
    }
}

