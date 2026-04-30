@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todocitas.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todocitas.data.local.entities.Cliente
import com.example.todocitas.navigation.Views
import com.example.todocitas.ui.theme.BackgroundDark
import com.example.todocitas.ui.theme.ErrorColor
import com.example.todocitas.ui.theme.OnSurfaceVariantCustom
import com.example.todocitas.ui.theme.OutlineColor
import com.example.todocitas.ui.theme.Primary
import com.example.todocitas.ui.theme.SurfaceContainer
import com.example.todocitas.ui.theme.SurfaceContainerHighest
import com.example.todocitas.ui.theme.ToDoCitasTheme
import com.example.todocitas.viewmodels.ClientesViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NuevaCitaView(
    navController: NavController,
    openDrawer: () -> Unit,
    clientesViewModel: ClientesViewModel = hiltViewModel()
) {
    // Colecta el estado de la búsqueda y los resultados del ViewModel
    val searchQuery by clientesViewModel.searchQuery.collectAsState()
    val clientesSugeridos by clientesViewModel.clientesPaginados.collectAsState()
    // Para controlar si la ventana flotante está abierta
    var isExpanded by remember { mutableStateOf(false) }
    // Estado para el cliente seleccionado
    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }

    var discountChecked by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Nueva Cita",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
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
                    .padding(paddingValues)
                    .padding(horizontal = 18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Spacer(modifier = Modifier.height(20.dp))

                // --- SECCIÓN CLIENTE ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "CLIENTE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                CustomInputBox(
                    value = searchQuery,
                    onValueChange = { query ->
                        clientesViewModel.onSearchQueryChange(query)},
                    placeholder = "Buscar cliente...",
                    leadingIcon = Icons.Default.Search,
                    trailingIcon =  Icons.Default.PersonAdd,
                    suggestions = clientesSugeridos,
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it },
                    onSuggestionSelected = { cliente ->
                        // Al seleccionar, actualiza el texto
                        clientesViewModel.onSearchQueryChange("${cliente.nombre} ${cliente.apellido}")
                        clienteSeleccionado = cliente
                        isExpanded = false // Cierra el menú
                    },
                    onTrailingIconClick = {
                        navController.navigate(Views.NuevoClienteView.route + "?clienteId=-1")
                        // Limpiar el texto
                        clientesViewModel.onSearchQueryChange("")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- FECHA Y HORA ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        SectionLabel("Fecha")
                        ReadOnlyInput(text = "24 Oct, 2023", icon = Icons.Default.CalendarToday)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        SectionLabel("Hora")
                        ReadOnlyInput(text = "10:30 AM", icon = Icons.Default.Schedule)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- ESTADO ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "ESTADO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                ReadOnlyInput(text = "Pendiente", icon = Icons.Default.ExpandMore, iconAtEnd = true)

                Spacer(modifier = Modifier.height(16.dp))

                // --- SECCIÓN SERVICIOS ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "SERVICIOS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = SurfaceContainerHighest,
                            shape = RoundedCornerShape(50),
                        ) {
                            Text(
                                "2",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceVariantCustom
                            )
                        }
                    }
                    TextButton(onClick = { /* TODO */ }) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "AÑADIR",
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Lista de Servicios con borde azul
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            2.dp, Primary.copy(alpha = 0.4f),
                            RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainer.copy(alpha = 0.3f))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ServiceRow("Corte de Cabello", "$25.00")
                    ServiceRow("Afeitado Express", "$15.00")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- RESUMEN BENTO BOX ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "RESUMEN TOTAL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            2.dp, Primary.copy(alpha = 0.4f),
                            RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainer.copy(alpha = 0.3f))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = discountChecked,
                                onCheckedChange = { discountChecked = it },
                                colors = CheckboxDefaults.colors(checkedColor = Primary)
                            )
                            Text("% Descuento", color = OnSurfaceVariantCustom, fontSize = 14.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("10", color = Primary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("%", color = Primary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal: $40.00", color = OnSurfaceVariantCustom, fontSize = 12.sp)
                        Text("- $4.00 (Desc.)", color = ErrorColor, fontSize = 12.sp)
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Primary.copy(alpha = 0.1f)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            "$40.00",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // FOOTER
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    BackgroundDark.copy(alpha = 0.8f),
                                    BackgroundDark
                                )
                            )
                        )
                        .padding(bottom = 24.dp, top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                    Button(
                        onClick = { /* Guardar */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Guardar Cita", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainer)
                    ) {
                        Text(
                            "Cancelar",
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariantCustom
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        color = OnSurfaceVariantCustom,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
    )
}

@Composable
fun CustomInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector,
    suggestions: List<Cliente>, // Clientes desde el VM
    expanded: Boolean,          // Controla si se ve la ventana
    onExpandedChange: (Boolean) -> Unit,
    onSuggestionSelected: (Cliente) -> Unit, // Hacer click en un cliente
    onTrailingIconClick: () -> Unit
) {
        // UEl Componente oficial para evitar que el teclado se cierre
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(it) },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    // Solo expandir si hay texto, para que el teclado no se cierre al vaciar
                    onExpandedChange(it.isNotEmpty())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor() // Vincula el menú al TextField (Material 3)
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = { Text(placeholder, color = OutlineColor) },
                leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = OutlineColor) },
                trailingIcon = {
                    IconButton(onClick = onTrailingIconClick) { //
                        Icon(
                            trailingIcon,
                            contentDescription = "Agregar Cliente",
                            tint = Primary
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceContainer,
                    unfocusedContainerColor = SurfaceContainer,
                    focusedIndicatorColor = Primary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Primary // Asegura que el cursor sea visible
                ),
                singleLine = true // Evita saltos de línea que muevan el layout
            )

            // El menú ahora se posiciona correctamente hacia abajo gracias al ExposedDropdownMenu
            if (suggestions.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    modifier = Modifier
                        .background(SurfaceContainerHighest)
                        .border(1.dp, Primary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                ) {
                    suggestions.forEach { cliente ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = "${cliente.nombre} ${cliente.apellido}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = cliente.telefono,
                                        color = OnSurfaceVariantCustom,
                                        fontSize = 12.sp
                                    )
                                }
                            },
                            onClick = {
                                onSuggestionSelected(cliente)
                                onExpandedChange(false)
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
    }

@Composable
fun ReadOnlyInput(text: String, icon: ImageVector, iconAtEnd: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            .background(SurfaceContainer)
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (iconAtEnd) Arrangement.SpaceBetween else Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!iconAtEnd) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(text, color = Color.White, fontSize = 14.sp)
            if (iconAtEnd) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = OutlineColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ServiceRow(name: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, modifier = Modifier.weight(1f), color = Color.White, fontSize = 14.sp)
        Text(price, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            tint = OutlineColor,
            modifier = Modifier
                .size(20.dp)
                .clickable { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101C22)
@Composable
fun NuevaCitaViewPreview() {
    ToDoCitasTheme {
        NuevaCitaView(NavController(LocalContext.current), openDrawer = {})
    }
}