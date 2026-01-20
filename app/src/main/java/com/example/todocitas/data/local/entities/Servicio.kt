package com.example.todocitas.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "nombre")
    val nombre: String,
    @ColumnInfo(name = "precio")
    val precio: Double,
    @ColumnInfo(name = "descripcion")
    val descripcion: String

)
