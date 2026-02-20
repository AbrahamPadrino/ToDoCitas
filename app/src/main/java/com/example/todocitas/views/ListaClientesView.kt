package com.example.todocitas.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.rotationMatrix
import androidx.navigation.NavController
import com.example.todocitas.R
import com.example.todocitas.components.SearchBar
import com.example.todocitas.data.local.entities.Cliente
import com.example.todocitas.ui.theme.*
import androidx.core.net.toUri // Import para convertir String a Uri
import coil.compose.AsyncImage
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaClientesView(
    onBack: () -> Unit,
    onAddNewClient: () -> Unit,
    clientes: List<Cliente>,
    navController: NavController,
    openDrawer: () -> Unit,
    onDeleteCliente: (Cliente) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedCardId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Lista de Clientes",
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
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAddNewClient) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = "Añadir Cliente",
                            tint = Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark.copy(alpha = 0.8f) // Efecto translúcido como en el diseño
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Barra de búsqueda
            SearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Buscar cliente...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (clientes.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(clientes.filter { it.nombre.contains(searchQuery, ignoreCase = true) }) { cliente ->
                        ClientCard(
                            cliente = cliente,
                            isExpanded = expandedCardId == cliente.id,
                            onExpand = {
                                // Si hacemos clic en una tarjeta ya expandida, la cerramos (null).
                                // Si no, la expandimos.
                                expandedCardId = if (expandedCardId == cliente.id) null else cliente.id
                            },
                            onDelete = {
                                onDeleteCliente(cliente)
                                expandedCardId = null
                            },
                            // Pasa aquí las otras acciones (onEdit, onCall, etc.)
                            onEdit = { /* Lógica de edición */ },
                            onCall = { /* Lógica de llamada */ },
                            onEmail = { /* Lógica de correo electrónico */ }

                        )
                    }

                    // Mensaje "No hay más clientes" al final de la lista
                    item {
                        EmptyState(isEndOfList = true)
                    }
                }
            }
        }
    }
}



@Composable
fun ClientCard(
    cliente: Cliente,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onCall: () -> Unit,
    onEmail: () -> Unit
) {
    // Animación de rotación para el icono de la flecha
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rotation")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardDark)
            .clickable(onClick = onExpand) // El clic en toda la tarjeta la expande/contrae
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Información del cliente
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = cliente.imagenUri?.toUri() ?: R.drawable.outline_account_circle_24,
                contentDescription = "Foto de ${cliente.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cliente.nombre,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = cliente.telefono,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expandir/Contraer",
                tint = TextSecondary,
                modifier = Modifier.rotate(rotationAngle) // Aplica la rotación animada
            )
        }

        // Botones de acción
        // AnimatedVisibility mostrará su contenido solo cuando `visible` sea true.
        AnimatedVisibility(visible = isExpanded) {
            Row(
                modifier = Modifier.padding(top = 8.dp), // Espacio extra cuando aparece
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Modificamos ActionButton para que acepte un onClick
                ActionButton(icon = Icons.Default.Call, isPrimary = true, modifier = Modifier.weight(1f), onClick = { /* Lógica de llamada */ })
                ActionButton(icon = Icons.Default.Email, modifier = Modifier.weight(1f), onClick = { /* Lógica de email */ })
                ActionButton(icon = Icons.Default.Edit, modifier = Modifier.weight(1f), onClick = onEdit)
                ActionButton(icon = Icons.Default.Delete, isDelete = true, modifier = Modifier.weight(1f), onClick = onDelete)
            }
        }

    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    isDelete: Boolean = false
) {
    val backgroundColor = when {
        isPrimary -> Primary.copy(alpha = 0.2f)
        else -> CardDark.copy(red = 0.4f, green = 0.45f, blue = 0.5f) // Un gris más oscuro
    }
    val iconColor = when {
        isPrimary -> Primary
        else -> TextSecondary
    }

    IconButton(
        onClick = { /* TODO: Implement action */ },
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
    ) {
        Icon(icon, contentDescription = null, tint = iconColor)
    }
}


@Composable
fun EmptyState(isEndOfList: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isEndOfList) 60.dp else 80.dp)
                .clip(CircleShape)
                .background(CardDark),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(if (isEndOfList) 30.dp else 40.dp)
            )
        }
        Text(
            text = "No hay más clientes",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 18.sp
        )
        Text(
            text = "Presiona '+' para agregar un nuevo cliente.",
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}


// --- Preview para Android Studio ---
@Preview(showBackground = true, backgroundColor = 0xFF101C22)
@Composable
fun ListaClientesViewPreview() {
    // Datos de ejemplo para la preview
    val sampleClients = listOf(
        Cliente(1, "Ana", "García Pérez","ana_email@gmail.com", "+34 600 123 456", "R.drawable.profile1"), // Reemplaza con tus imágenes o la URI de tu recurso
        Cliente(2, "Carlos", "Martinez", "Carlos2026@gmail.com", "+34 601 234 567", "R.drawable.profile2"),
        Cliente(3, "Laura", "Fernandez","laura_email@gmail.com", "+34 602 345 678", "R.drawable.profile10")
    )
    ToDoCitasTheme {
        ListaClientesView(
            onBack = {},
            onAddNewClient = {},
            clientes = sampleClients,
            navController = NavController(LocalContext.current),
            openDrawer = {},
            onDeleteCliente = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101C22)
@Composable
fun ListaClientesEmptyPreview() {
    ToDoCitasTheme {
        ListaClientesView(
            onBack = {},
            onAddNewClient = {},
            clientes = emptyList(),
            navController = NavController(LocalContext.current),
            openDrawer = {},
            onDeleteCliente = {}
        )
    }
}