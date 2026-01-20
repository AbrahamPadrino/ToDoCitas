package com.example.todocitas.data.local.dao

import androidx.room.*
import com.example.todocitas.data.local.entities.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface CitasDao {
    // --- Operaciones básicas de Citas ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCita(cita: Cita): Long // Devuelve el ID de la cita insertada

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCitaServicioCrossRef(crossRef: CitaServicioCrossRef)

    @Transaction
    @Query("SELECT * FROM citas WHERE id = :citaId")
    suspend fun getCitaConServicios(citaId: Int): CitaConServicios

    @Query("UPDATE citas SET estado = :nuevoEstado WHERE id = :citaId")
    suspend fun updateCitaState(citaId: Int, nuevoEstado: CitaState)

    @Query("DELETE FROM citas WHERE id = :citaId")
    suspend fun deleteCita(citaId: Int)


    // --- CONSULTAS PARA REPORTES FINANCIEROS ---

    /**
     * Obtiene la suma de 'precioTotal' de todas las citas 'REALIZADAS'
     * entre dos fechas (timestamps).
     * Esta única consulta sirve para reportes diarios, semanales y mensuales.
     */
    @Query("""
        SELECT SUM(precioTotal)
        FROM citas
        WHERE estado = 'REALIZADA' AND fecha BETWEEN :fechaInicio AND :fechaFin
    """)
    fun getGananciasPorPeriodo(fechaInicio: Date, fechaFin: Date): Flow<Double?>

}