package com.example.todocitas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todocitas.data.local.entities.Servicio
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiciosDao {
    /**
     * Usa Flow para obtener la lista de servicios.
     * La UI se actualizará automáticamente cada vez que haya un cambio en la tabla.
     */
    @Query("SELECT * FROM servicios ORDER BY nombre ASC")
    fun getAllServicios(): Flow<List<Servicio>>

    /**
     * Obtiene un servicio por su ID.
     */
    @Query("SELECT * FROM servicios WHERE id = :id")
    suspend fun getServicioById(id: Int): Servicio?

    /**
     * Inserta un nuevo servicio. Si ya existe (conflicto de PrimaryKey), lo reemplaza.
     * Es una función 'suspend' porque es una operación de I/O y no debe bloquear el hilo principal.
     */
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertServicio(servicio: Servicio)

    /**
     * Actualiza un servicio existente.
     */
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun updateServicio(servicio: Servicio)

    /**
     * Borra un servicio. También es una función 'suspend'.
     */
    @Delete
    suspend fun deleteServicio(servicio: Servicio)
}