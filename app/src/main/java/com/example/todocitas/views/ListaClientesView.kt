package com.example.todocitas.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todocitas.R
import com.example.todocitas.components.SearchBar
import com.example.todocitas.data.local.entities.Cliente
import com.example.todocitas.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaClientesView(
    onBack: () -> Unit,
    onAddNewClient: () -> Unit,
    clientes: List<Cliente>,
    navController: NavController,
    openDrawer: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

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
                        ClientCard(cliente = cliente)
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
fun ClientCard(cliente: Cliente) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Información del cliente
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile1),
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
        }

        // Botones de acción
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionButton(icon = Icons.Default.Call, isPrimary = true, modifier = Modifier.weight(1f))
            ActionButton(icon = Icons.Default.Email, modifier = Modifier.weight(1f))
            ActionButton(icon = Icons.Default.Edit, modifier = Modifier.weight(1f))
            ActionButton(icon = Icons.Default.Delete, isDelete = true, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
        Cliente(1, "Ana García Pérez", "+34 600 123 456", "R.drawable.profile1"), // Reemplaza con tus imágenes o la URI de tu recurso
        Cliente(2, "Carlos Martinez", "+34 601 234 567", "R.drawable.profile2"),
        Cliente(3, "Laura Fernandez", "+34 602 345 678", "R.drawable.profile10")
    )
    ToDoCitasTheme {
        ListaClientesView(
            onBack = {},
            onAddNewClient = {},
            clientes = sampleClients,
            navController = NavController(LocalContext.current),
            openDrawer = {}
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
            openDrawer = {}
        )
    }
}