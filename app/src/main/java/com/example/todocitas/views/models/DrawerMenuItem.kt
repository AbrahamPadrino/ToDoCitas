package com.example.todocitas.views.models

import androidx.compose.ui.graphics.vector.ImageVector

// Data class para modelar cada item del menú
data class DrawerMenuItem(
    val icon: ImageVector,
    val text: String,
    val route: String
)