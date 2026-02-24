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

    @Query("SELECT * FROM clientes ORDER BY nombre ASC LIMIT :limit OFFSET :offset")
    fun getClientesPaginados(limit: Int, offset: Int): Flow<List<Cliente>>

    @Query("SELECT COUNT(id) FROM clientes")
    fun countClientes(): Flow<Int>

    // FUNCIÓN PARA BÚSQUEDA PAGINADA
    // Busca clientes cuyo nombre contenga el 'query' y devuelve una página de resultados.
    @Query("""
        SELECT * FROM clientes
        WHERE nombre LIKE '%' || :query || '%'
        ORDER BY nombre ASC
        LIMIT :limit OFFSET :offset
    """)
    fun searchAndPaginateClientes(query: String, limit: Int, offset: Int): Flow<List<Cliente>>

    // FUNCIÓN PARA CONTAR LOS RESULTADOS DE LA BÚSQUEDA
    // Es crucial para recalcular el número total de páginas según los resultados del filtro.
    @Query("SELECT COUNT(id) FROM clientes WHERE nombre LIKE '%' || :query || '%'")
    fun countSearchResults(query: String): Flow<Int>

}