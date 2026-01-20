package com.example.todocitas.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todocitas.data.local.dao.CitasDao
import com.example.todocitas.data.local.dao.ClientesDao
import com.example.todocitas.data.local.dao.ServiciosDao
import com.example.todocitas.data.local.entities.Cita
import com.example.todocitas.data.local.entities.CitaServicioCrossRef
import com.example.todocitas.data.local.entities.Servicio
import com.example.todocitas.data.local.entities.Cliente


@Database(
    entities = [Cliente::class, Servicio::class, Cita::class, CitaServicioCrossRef::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun clientesDao(): ClientesDao

    abstract fun serviciosDao(): ServiciosDao

    abstract fun citasDao(): CitasDao

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstanceDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "citas_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}