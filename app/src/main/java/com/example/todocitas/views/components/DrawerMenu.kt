package com.example.todocitas.views.components

import com.example.todocitas.views.models.DrawerMenuItem
import com.example.todocitas.views.models.DrawerMenuSection
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todocitas.R
import com.example.todocitas.navigation.Views
import com.example.todocitas.ui.theme.*


/**
 * El contenido completo del menú lateral (Drawer)
 */
@Composable
fun DrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val menuSections = listOf(
        DrawerMenuSection(
            "CLIENTES", listOf(
                DrawerMenuItem(
                    Icons.Outlined.Group,
                    "Listar Clientes",
                    Views.ListaClientesView.route
                ),
                DrawerMenuItem(
                    Icons.Outlined.PersonAdd,
                    "Nuevo Cliente",
                    Views.NuevoClienteView.route
                )
            )
        ),
        DrawerMenuSection(
            "SERVICIOS", listOf(
                DrawerMenuItem(
                    Icons.Outlined.ContentPaste,
                    "Listar Servicios",
                    Views.ListaServiciosView.route
                ),
                DrawerMenuItem(
                    Icons.Outlined.AddCircleOutline,
                    "Nuevo Servicio",
                    Views.NuevoServicioView.route
                )
            )
        ),
        DrawerMenuSection(
            "MIS CITAS", listOf(
                DrawerMenuItem(
                    Icons.Outlined.CalendarMonth,
                    "Listar Citas",
                    Views.ListaCitasView.route
                ),
                DrawerMenuItem(
                    Icons.Outlined.EditCalendar,
                    "Nueva Cita",
                    Views.NuevaCitaView.route
                )
            )
        ),
        DrawerMenuSection(
            "REPORTES", listOf(
                DrawerMenuItem(
                    Icons.Outlined.ConfirmationNumber,
                    "Ver Ticket",
                    Views.ReporteTicketView.route
                ),
                DrawerMenuItem(
                    Icons.Outlined.BarChart,
                    "Resumen Semanal",
                    Views.ReporteSemanalView.route
                ),
                DrawerMenuItem(
                    Icons.Outlined.PieChart,
                    "Resumen Mensual",
                    Views.ReporteMensualView.route
                )
            )
        )

        // Añade aquí más secciones como "MIS CITAS", "REPORTES", etc.
    )

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileHeader()

        // Botón de INICIO
        DrawerItem(
            icon = Icons.Default.Home,
            text = "INICIO",
            isSelected = currentRoute == Views.InicioView.route,
            onClick = { onNavigate(Views.InicioView.route) },
            isHeader = true
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            menuSections.forEach { section ->
                item {
                    Text(
                        text = section.title,
                        color = TextSecondary.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp),
                        letterSpacing = 0.08.sp
                    )
                }
                items(section.items.size) { index ->
                    val item = section.items[index]
                    DrawerItem(
                        icon = item.icon,
                        text = item.text,
                        isSelected = currentRoute == item.route,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }
        }

        Footer(onLogout = onLogout)
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Primary.copy(alpha = 0.05f),
                        CardDark.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                )
            )
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Reemplaza con una imagen real
                    contentDescription = "User Portrait",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, Primary.copy(alpha = 0.3f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(14.dp)
                        .background(Color(0xFF2ECC71), CircleShape)
                        .border(2.dp, BackgroundDark, CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "John Doe",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    "ADMINISTRADOR",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.1.sp
                )
            }
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isHeader: Boolean = false
) {
    val backgroundColor = if (isSelected) Primary.copy(alpha = 0.1f) else Color.Transparent
    val contentColor = if (isSelected) Primary else TextSecondary
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isHeader) 8.dp else 0.dp)
            .height(if (isHeader) 48.dp else 44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = contentColor,
            fontWeight = fontWeight,
            fontSize = if (isHeader) 14.sp else 14.sp,
            letterSpacing = if (isHeader) 0.05.sp else 0.sp
        )
    }
}

@Composable
private fun Footer(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = BorderDark.copy(alpha = 0.5f),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(top = 1.dp)
    ) {
        DrawerItem(
            icon = Icons.Outlined.AccountCircle,
            text = "MI PERFIL",
            isSelected = false,
            onClick = { /* Navegar a perfil */ }
        )
        HorizontalDivider(color = BorderDark.copy(alpha = 0.5f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "v2.4.0",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.clickable(onClick = onLogout),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = "Salir",
                    tint = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "SALIR",
                    color = Color.Red.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
