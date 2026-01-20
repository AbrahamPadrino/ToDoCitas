/*Análisis de Cita:
•   foreignKeys: Le decimos a Room que clienteId está vinculado a la columna id de la tabla clientes.
•   onDelete = ForeignKey.CASCADE: Esta es una regla de integridad crucial. Si eliminas un cliente de
    tu base de datos, Room se encargará de eliminar automáticamente todas las citas asociadas a ese
    cliente. ¡Muy potente!
•   fecha: Date: Usamos el tipo java.util.Date para almacenar la fecha y hora de la cita. Room sabe
    cómo manejarlo.
 */

package com.example.todocitas.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.todocitas.data.local.room.Converters
import java.util.Date

@Entity(
    tableName = "citas",
    foreignKeys = [
        ForeignKey(
            entity = Cliente::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.CASCADE // Si se borra un cliente, se borran sus citas.
        )
    ]
)

@TypeConverters(Converters::class) // Le decimos a Room cómo manejar Date y CitaState
data class Cita(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clienteId: Int, // Clave foránea que apunta al Cliente
    val fecha: Date, // Room puede manejar tipos Date. Los convierte a Long (timestamp) internamente.
    val notas: String? = null, // Un campo opcional para notas adicionales.
    val precioTotal: Double, // Precio total calculado y guardado en el momento de la creación.
    val estado: CitaState = CitaState.PENDIENTE // Por defecto, una nueva cita está pendiente.
)
