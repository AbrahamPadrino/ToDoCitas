package com.example.todocitas.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "nombre")
    val nombre: String,
    @ColumnInfo(name = "telefono")
    val telefono: String,
    @ColumnInfo(name = "imagenResId")
    val imagenResId: Int // Usamos un ID de recurso drawable para la imagen
)