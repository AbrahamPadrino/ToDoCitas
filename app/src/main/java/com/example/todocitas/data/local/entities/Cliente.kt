package com.example.todocitas.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "nombre")
    val nombre: String,
    @ColumnInfo(name = "apellido")
    val apellido: String,
    @ColumnInfo(name = "correo")
    val correo: String,
    @ColumnInfo(name = "telefono")
    val telefono: String,
    @ColumnInfo(name = "imagenUri")
    val imagenUri: String? // Usa un String para almacenar la URI de la imagen
)