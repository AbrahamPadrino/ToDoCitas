package com.example.todocitas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todocitas.data.local.entities.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientesDao {
    @Query("SELECT * FROM clientes")
    fun getClientes(): Flow<List<Cliente>>

    @Query("SELECT * FROM clientes WHERE id = :id")
    suspend fun getClienteById(id: Int): Cliente?

    @Insert
    suspend fun insertCliente(cliente: Cliente)

    @Update
    suspend fun updateCliente(cliente: Cliente)

    @Delete
    suspend fun deleteCliente(cliente: Cliente)

}