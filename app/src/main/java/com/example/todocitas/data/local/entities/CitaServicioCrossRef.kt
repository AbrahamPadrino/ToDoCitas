/*Análisis de CitaServicioCrossRef:
•   primaryKeys = [...]: Definimos una clave primaria compuesta. Esto asegura que no puedas
    añadir el mismo servicio a la misma cita más de una vez.
*/

package com.example.todocitas.data.local.entities

import androidx.room.Entity

@Entity(tableName = "cita_servicio_cross_ref", primaryKeys = ["citaId", "servicioId"])
data class CitaServicioCrossRef(
    val citaId: Int,
    val servicioId: Int
)