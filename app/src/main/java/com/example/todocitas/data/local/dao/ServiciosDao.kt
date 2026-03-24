package com.example.todocitas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
     * Inserta un nuevo servicio.
     * Es una función 'suspend' porque es una operación de I/O y no debe bloquear el hilo principal.
     */
    @Insert
    suspend fun insertServicio(servicio: Servicio)

    /**
     * Actualiza un servicio existente.
     */
    //@Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    @Update
    suspend fun updateServicio(servicio: Servicio)

    /**
     * Borra un servicio. También es una función 'suspend'.
     */
    @Delete
    suspend fun deleteServicio(servicio: Servicio)

    /**
     * Consultas para paginar y buscar servicios.
     */
    //    LIMIT: cuántos registros traer.
    //    OFFSET: cuántos registros saltar desde el inicio.
    @Query("SELECT * FROM servicios ORDER BY nombre ASC LIMIT :limit OFFSET :offset")
    fun getServiciosPaginados(limit: Int, offset: Int): Flow<List<Servicio>>

    @Query("SELECT COUNT(id) FROM servicios")
    fun countServicios(): Flow<Int>

    /**
     * Busqueda paginada y filtrada.
     */
    // Busca servicios cuyo nombre contenga el 'query' y devuelve una página de resultados.
    @Query("""
        SELECT * FROM servicios
        WHERE nombre LIKE '%' || :query || '%'
        LIMIT :limit OFFSET :offset
    """)
    fun searchAndPaginateServicios(query: String, limit: Int, offset: Int): Flow<List<Servicio>>

    // Crucial para recalcular el número total de páginas según los resultados del filtro.
    @Query("SELECT COUNT(id) FROM servicios WHERE nombre LIKE '%' || :query || '%'")
    fun countSearchResults(query: String): Flow<Int>



}