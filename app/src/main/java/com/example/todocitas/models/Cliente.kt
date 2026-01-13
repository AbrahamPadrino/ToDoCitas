package com.example.todocitas.models

data class Cliente(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val imagenResId: Int // Usamos un ID de recurso drawable para la imagen
)