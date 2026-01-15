package com.example.todocitas.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todocitas.models.Cliente

@Database(
    entities = [Cliente::class],
    version = 1,
    exportSchema = false
)
abstract class ClientesDatabase: RoomDatabase() {
    abstract fun clientesDao(): ClientesDao
}