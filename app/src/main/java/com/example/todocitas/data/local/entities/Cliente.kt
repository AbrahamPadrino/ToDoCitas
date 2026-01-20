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
    @ColumnInfo(name = "telefono")
    val telefono: String,
    @ColumnInfo(name = "imagenUri")
    val imagenUri: String? // Usa un String para almacenar la URI de la imagen
)
/*
Como proximo paso quiero preparar una nueva entidad para las citas generadas
a partir de un cliente que se haya registrado a traves de  @Cliente.kt  y
seleccione uno o varios servicios de  @Servicio.kt  por esa razó te pido
que actúes como un experto de base de datos y me ayudes a
encontrar la solución mas optima para este caso.*/
