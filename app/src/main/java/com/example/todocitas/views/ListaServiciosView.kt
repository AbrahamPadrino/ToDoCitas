package com.example.todocitas.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todocitas.components.SearchBar
import com.example.todocitas.data.local.entities.Servicio
import com.example.todocitas.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaServiciosView(
    onBack: () -> Unit,
    onAddNewService: () -> Unit,
    servicios: List<Servicio>,
    paginaActual: Int,
    totalPaginas: Int,
    onCambiarPagina: (Int) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    navController: NavController,
    openDrawer: () -> Unit,
    onEditServicio: (Servicio) -> Unit,
    onDeleteServicio: (Servicio) -> Unit
) {
    var expandedCardId by remember { mutableStateOf<Int?>(null) }
    var servicioAEliminar by remember { mutableStateOf<Servicio?>(null) }

    LaunchedEffect(paginaActual) {
        // Al cambiar de página, cierra cualquier tarjeta que estuviera expandida.
        expandedCardId = null
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Lista de Servicios",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp,
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
                    // Espaciador para centrar el título correctamente
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    scrolledContainerColor = BackgroundDark.copy(alpha = 0.8f) // Efecto blur al hacer scroll
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNewService,
                containerColor = Primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Servicio", modifier = Modifier.size(28.dp))
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            // Barra de búsqueda (reutilizamos la de ListaClientesView)
            SearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Buscar servicio...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            // Lista de servicios

            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp), // Padding para el FAB
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(servicios.filter { it.nombre.contains(searchQuery, ignoreCase = true) }) { servicio ->
                    ServiceCard(
                        servicio = servicio,
                        isExpanded = expandedCardId == servicio.id,
                        onExpand = {
                            expandedCardId = if (expandedCardId == servicio.id) null else servicio.id
                        },

                        onEdit = { onEditServicio(servicio) },

                        onDelete = { servicioAEliminar = servicio }
                    )
                }
            }
            if (totalPaginas > 1) {
                PaginacionControles(
                    paginaActual = paginaActual,
                    totalPaginas = totalPaginas,
                    onCambiarPagina = onCambiarPagina
                )
            }
        }
    }

    // Dialogo para Confirmar Eliminar
    servicioAEliminar?.let { servicio ->
        AlertDialog(
            onDismissRequest = {servicioAEliminar = null},
            containerColor = CardDark,
            title = {
                Text(
                    text = "Confirmar Eliminación",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = buildAnnotatedString {
                        append("¿Seguro deseas eliminar: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(servicio.nombre)
                        }
                        append("? Esta acción no se puede deshacer.")

                    },
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteServicio(servicio)
                        servicioAEliminar = null
                        // Cierra la Card expandida si estaba abierta.
                        expandedCardId = null
                    }
                ) {
                    Text("ACEPTAR", color = TextAlert, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Simplemente cierra el diálogo.
                        servicioAEliminar = null
                        expandedCardId = null
                    }
                ) {
                    Text("CANCELAR", color = Primary, fontWeight = FontWeight.Normal)
                }
            }
        )
    }

}
@Composable
fun ServiceCard(
    servicio: Servicio,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    // Animación de rotación para el icono de la flecha
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardDark)
            .clickable(onClick = onExpand) // El clic en toda la tarjeta la expande/contrae
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Información del servicio
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = servicio.nombre,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
                Text(
                    text = String.format("$%.2f", servicio.precio),
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = servicio.descripcion,
                    color = TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
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
            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActionButton(
                        icon = Icons.Default.Edit,
                        modifier = Modifier.weight(1f),
                        onClick = onEdit
                    )
                    ActionButton(
                        icon = Icons.Default.Delete,
                        isDelete = true,
                        modifier = Modifier.weight(1f),
                        onClick = onDelete
                    )
                }
            }
    }
}

// --- Preview para Android Studio ---
@Preview(showBackground = true, backgroundColor = 0xFF101C22)
@Composable
fun ListaServiciosViewPreview() {
    val sampleServices = listOf(
        Servicio(1, "Corte de Cabello Caballero", 25.00, "Servicio completo que incluye lavado y peinado con productos premium."),
        Servicio(2, "Barba y Bigote", 15.00, "Perfilado con navaja y aplicación de aceites hidratantes para un acabado impecable."),
        Servicio(3, "Tratamiento Capilar", 40.00, "Hidratación profunda para fortalecer el cuero cabelludo y prevenir la caída."),
        Servicio(4, "Manicura Express", 20.00, "Limpieza, limado y pulido de uñas con acabado natural para manos impecables."),
        Servicio(5, "Tinte de Cabello", 55.00, "Coloración profesional con productos de larga duración y protección térmica.")
    )
    ToDoCitasTheme {
        ListaServiciosView(
            onBack = {},
            onAddNewService = {},
            servicios = sampleServices,
            paginaActual = 1,
            totalPaginas = 1,
            onCambiarPagina = {},
            searchQuery = "",
            onSearchQueryChange = {},
            navController = NavController(LocalContext.current),
            openDrawer = {},
            onEditServicio = {},
            onDeleteServicio = {}
        )
    }
}