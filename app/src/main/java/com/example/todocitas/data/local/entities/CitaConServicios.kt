/*Análisis de CitaConServicios:
•   @Embedded: Le dice a Room que incluya todas las columnas del objeto Cita
    como si estuvieran en esta clase.
•   @Relation: Esta es la anotación clave. Define cómo se conectan la Cita y los Servicios.
•   associateBy = Junction(...): Aquí le especificamos a Room que debe usar nuestra
    tabla de unión CitaServicioCrossRef para encontrar los servicios correspondientes
    a cada cita.
*/

package com.example.todocitas.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CitaConServicios(
    @Embedded val cita: Cita, // La cita principal
    @Relation(
        parentColumn = "id", // ID de la Cita
        entityColumn = "id", // ID del Servicio
        associateBy = Junction(
            value = CitaServicioCrossRef::class,
            parentColumn = "citaId", // Columna en la tabla de unión que apunta a la Cita
            entityColumn = "servicioId" // Columna en la tabla de unión que apunta al Servicio
        )
    )
    val servicios: List<Servicio> // La lista de servicios asociados
)