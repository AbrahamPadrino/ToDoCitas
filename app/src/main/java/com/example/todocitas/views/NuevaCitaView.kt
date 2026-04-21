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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todocitas.ui.theme.BackgroundDark
import com.example.todocitas.ui.theme.ErrorColor
import com.example.todocitas.ui.theme.OnSurfaceVariantCustom
import com.example.todocitas.ui.theme.OutlineColor
import com.example.todocitas.ui.theme.Primary
import com.example.todocitas.ui.theme.SurfaceContainer
import com.example.todocitas.ui.theme.SurfaceContainerHighest
import com.example.todocitas.ui.theme.ToDoCitasTheme



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NuevaCitaView(
    navController: NavController,
    openDrawer: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- SECCIÓN CLIENTE ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
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
                onValueChange = { searchQuery = it },
                placeholder = "Buscar cliente...",
                leadingIcon = Icons.Default.Search,
                trailingIcon = Icons.Default.PersonAdd
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
                verticalAlignment = Alignment.CenterVertically) {
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
                    Text("AÑADIR", color = Primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
                verticalAlignment = Alignment.CenterVertically) {
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

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    Text(
                        "$40.00",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- ACCIONES FINALES ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainer)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold, color = OnSurfaceVariantCustom)
                }
                Button(
                    onClick = { /* Guardar */ },
                    modifier = Modifier
                        .weight(2f)
                        .height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Guardar Cita", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
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
    trailingIcon: ImageVector
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)), // Asegúrate de que RoundedCornerShape esté importado
        placeholder = { Text(placeholder, color = OutlineColor) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = OutlineColor) },
        trailingIcon = { Icon(trailingIcon, contentDescription = null, tint = Primary) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SurfaceContainer,
            unfocusedContainerColor = SurfaceContainer,
            disabledContainerColor = SurfaceContainer,
            focusedIndicatorColor = Primary,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
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
                Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(text, color = Color.White, fontSize = 14.sp)
            if (iconAtEnd) {
                Icon(icon, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(20.dp))
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
            modifier = Modifier.size(20.dp).clickable { }
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