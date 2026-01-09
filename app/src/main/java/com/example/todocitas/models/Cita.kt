package com.example.todocitas.models

data class Cita(
    val nombre: String,
    val hora: String,
    val servicio: String,
    val imageUrl: String? = null,
    val isOnline: Boolean = false,
    val initials: String? = null,
    val opacity: Float = 1f
)