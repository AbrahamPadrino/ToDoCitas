package com.example.todocitas.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todocitas.R
import com.example.todocitas.models.Cita
import com.example.todocitas.models.DiaCalendario
import com.example.todocitas.ui.theme.BackgroundDark
import com.example.todocitas.ui.theme.BorderDark
import com.example.todocitas.ui.theme.CardDark
import com.example.todocitas.ui.theme.Primary
import com.example.todocitas.ui.theme.TextSecondary
import com.example.todocitas.ui.theme.ToDoCitasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioView(
    navController: NavController,
    openDrawer: () -> Unit
) {
    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Gestión de Citas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundDark
                ),
                // Ajuste para simular el padding del HTML (pr-12 en el título)
                actions = { Spacer(modifier = Modifier.width(48.dp)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add action */ },
                containerColor = Primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(56.dp)
                    .padding(bottom = 8.dp, end = 8.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Añadir",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Sección "Ahora"
            SeccionAhora()

            // Sección "Calendario"
            SeccionCalendario()

            // Sección "Próximas Citas"
            SeccionProximasCitas()

            Spacer(modifier = Modifier.height(80.dp))
             // Espacio para el FAB y scroll
        }
    }
}

@Composable
fun SeccionAhora() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Ahora",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardDark),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                // Imagen de cabecera con gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.barbershop_header_image),
                        contentDescription = "Imagen de la cita",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Overlay Gradiente
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )
                    // Badge "En curso"
                    Surface(
                        color = Primary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .border(1.dp, Primary.copy(alpha = 0.3f), RoundedCornerShape(50))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Primary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "En curso",
                                color = Primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Detalles de la cita "Ahora"
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                "Juan Pérez",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Corte de Cabello Clásico",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "14:30",
                                color = Primary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text("PM", color = Color.Gray, fontSize = 12.sp)
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 1.dp,
                        color = BorderDark
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Ver detalles", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BorderDark)
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeccionCalendario() {
    val dias = listOf(
        DiaCalendario("Mie", "24", true),
        DiaCalendario("Jue", "25", false),
        DiaCalendario("Vie", "26", false),
        DiaCalendario("Sab", "27", false),
        DiaCalendario("Dom", "28", false)
    )

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Calendario",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "Hoy",
                color = Primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        LazyRow(
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dias) { dia ->
                Column(
                    modifier = Modifier
                        .size(width = 64.dp, height = 84.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (dia.isSelected) Primary else CardDark)
                        .then(
                            if (!dia.isSelected) Modifier.border(
                                1.dp,
                                BorderDark,
                                RoundedCornerShape(12.dp)
                            ) else Modifier
                        )
                        .clickable { },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        dia.nombre,
                        color = if (dia.isSelected) Color.White.copy(alpha = 0.8f) else TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        dia.numero,
                        color = Color.White,
                        fontSize = if (dia.isSelected) 24.sp else 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SeccionProximasCitas() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            "Próximas Citas",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
        )

        val citas = listOf(
            Cita(
                "María López",
                "15:00",
                "Tinte y secado",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuCcfaFnjukD-5ttC5r4PfwLfIkguT4IywgLtDh6OCnKOTIgW-3BToFfqmFuXtXQmk--x19QOnVepc2TeLRjLxUUQeRkU8ybWoloxXQRXFPg644qmgc8R6EGmWDkV6u1mRRNItvD-iTUtsqDaxiATUFatnBSeIp_dG5td8A_rT1447l62CsGlnj6zrPO3HGXnrvLXL5zBsmH-XU5PPCAPrP-KsxoMswoGk22ARWsf2zjh8Xd1411jMPtIyNqxJFMEJsR34vHJ7K7dMo",
                true
            ),
            Cita(
                "Carlos Ruiz",
                "16:30",
                "Perfilado de Barba",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuA_QcyTXM4gGK3nS3-C7XXoMFU5Utl0WXCTUx6_QhHnsLjJZix2UBfNXOsS3q6HjuRtsiQydA7dkZZcVJX-hmSHEcW5RdU-grHyNCiKUPxSAemB0C498qqUM8sZerQq6lrtzS0LNa0u0XcRe_gdbRRtA7ak13qSQjXpwbXWzg4LZMYbU2UeqMS3SqPT6qrIHe1oipePHcNLTyXBwbYQBWQ1RDECgv8SfBHgJzzJxY640llOtuOfnO7UkKvPRmWmNFpPWIcJ3qo9rDI"
            ),
            Cita(
                "Ana García",
                "17:15",
                "Peinado boda",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuDUVlXWF67j_xqICqzvm1Ser-Rkh7siOVGTku6yepnYsBpu7mhHLlGzUuxGb9LM76qYRxHq5ee7mArg4hM2CsdunZwtd_agxKgrsd07ubehO6JneeumizZ9ESHoHzeqR76ejrR_Hjy6cLZRuHepFsfBK5OSQDLEzYaeAEMJCr00V_KrvKfo6w9YSwjB_mksuVPRG_2J4K6luEr6RVJ_gp3P3uRIH5fWXktLlvHmH4ivJCCM7UW9vvGQmHEXX7wA0HGia0H-ke3d30I"
            ),
            Cita("Luis Torres", "18:00", "Consulta", initials = "LT", opacity = 0.6f)
        )

        citas.forEach { cita ->
            ItemCita(cita)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ItemCita(cita: Cita) {
    Surface(
        color = CardDark.copy(alpha = cita.opacity),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con indicador de estado si aplica
            Box {
                if (cita.imageUrl != null) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF374151), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            cita.initials ?: "",
                            color = Color.LightGray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (cita.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .background(Color(0xFF22C55E), CircleShape)
                            .border(2.dp, CardDark, CircleShape)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    cita.nombre,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Surface(
                        color = if (cita.opacity < 1f) Color.White.copy(alpha = 0.1f) else Primary.copy(
                            alpha = 0.1f
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            cita.hora,
                            color = if (cita.opacity < 1f) Color.Gray else Primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                horizontal = 6.dp,
                                vertical = 2.dp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        cita.servicio,
                        color = TextSecondary,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }
            }

            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioViewPreview() {
    ToDoCitasTheme(darkTheme = true) {
        InicioView(
            NavController(LocalContext.current),
            openDrawer = {}
        )
    }
}