package com.example.todocitas.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todocitas.components.CustomTextField
import com.example.todocitas.data.local.entities.Servicio
import com.example.todocitas.ui.theme.*
import com.example.todocitas.viewmodels.ServiciosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoServicioView(
    servicioId: Int,
    onBack: () -> Unit,
    navController: NavController,
    openDrawer: () -> Unit,
    onSaveService: (Servicio) -> Unit = {},
    onSaveComplete: () -> Unit,
    serviciosViewModel: ServiciosViewModel = hiltViewModel()
) {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    var mostrarDialogo by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Obtener el estado de error del ViewModel
    val errorState = serviciosViewModel.errorState

    // Carga de datos si estamos en modo edición
    LaunchedEffect(key1 = servicioId) {
        if (servicioId != -1) {
            // Si está en modo edición, pide al ViewModel que cargue los datos del cliente.
            val servicio = serviciosViewModel.getServicioById(servicioId)
            servicio?.let {
                nombre = it.nombre
                precio = it.precio.toString()
                descripcion = it.descripcion
            }
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            CenterAlignedTopAppBar(
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
                    IconButton(onClick = openDrawer) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "menu",
                            tint = Color.White, // El diseño original lo muestra en color primario, pero en dark theme se ve mejor blanco.
                            modifier = Modifier.size(28.dp)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                        onValueChange = {
                            nombre = it
                            serviciosViewModel.onNombreChange() // Limpia el error al escribir //
                        },
                        label = "Nombre",
                        placeholder = "Ej. Corte de pelo",
                        singleLine = true,
                        isError = errorState.nombreError != null, // <--- Conexión
                        errorMessage = errorState.nombreError,    // <--- Conexión
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next // Cambia la tecla enter por "Siguiente"
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) } // Mueve el foco abajo
                        )

                    )
                    CustomTextField(
                        value = precio,
                        onValueChange = {
                            precio = it
                            serviciosViewModel.onPrecioChange()
                        },
                        label = "Precio",
                        placeholder = "0.00",
                        isError = errorState.precioError != null,
                        errorMessage = errorState.precioError,
                        leadingIcon = { Text("$", color = TextSecondary, fontSize = 16.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )

                    )
                    CustomTextField(
                        value = descripcion,
                        onValueChange = {
                            descripcion = it
                            serviciosViewModel.onDescripcionChange()
                        },
                        label = "Descripción",
                        placeholder = "Describe brevemente el servicio...",
                        isError = errorState.descripcionError != null,
                        errorMessage = errorState.descripcionError,
                        singleLine = false,
                        modifier = Modifier.height(120.dp), // Altura para el campo de texto multi-línea
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done // Cambia la tecla enter por "Hecho"
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() } // Oculta el foco y el teclado
                        )
                    )
                }

                // Espaciador para dejar sitio a los botones flotantes
                Spacer(modifier = Modifier.height(200.dp))
            }
            // Footer con botones y gradiente
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
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
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            when {
                                serviciosViewModel.validarCampos(nombre, precio, descripcion) -> {
                                    if (servicioId == -1) {
                                        // Modo Creación
                                        serviciosViewModel.agregarServicio(
                                            Servicio(
                                                nombre = nombre,
                                                precio = precio.toDoubleOrNull() ?: 0.0,
                                                descripcion = descripcion
                                            )
                                        )
                                    } else {
                                        // Modo Edición
                                        serviciosViewModel.updateServicio(
                                            Servicio(
                                                id = servicioId, // ¡Mantenemos el ID original!
                                                nombre = nombre,
                                                precio = precio.toDoubleOrNull() ?: 0.0,
                                                descripcion = descripcion
                                            )
                                        )
                                    }
                                    mostrarDialogo = true
                                }
                            }
                        }, // fin onClick
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (servicioId == -1) "Guardar Servicio" else "Actualizar Servicio",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp)
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

    // Dialogo de confirmación
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
                    if (servicioId == -1) "Servicio guardado con éxito" else "Servicio actualizado con éxito",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        onSaveComplete() // Uso de callback para navegar hacia atrás
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
fun NuevoServicioViewPreview() {
    ToDoCitasTheme {
        NuevoServicioView(
            servicioId = TODO(),
            onSaveService = {},
            onSaveComplete = {},
            navController = NavController(LocalContext.current),
            openDrawer = {},
            onBack = {}
        )
    }
}