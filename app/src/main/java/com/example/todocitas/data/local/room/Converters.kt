/* Análisis de Converters:
    Room no sabe cómo guardar un Date o nuestro CitaState directamente en la base de datos.
    Necesitamos "traductores" (TypeConverters) que le digan cómo convertir estos objetos a
    tipos primitivos que sí entiende (como Long para fechas y String para el enum).
*/

package com.example.todocitas.data.local.room

import androidx.room.TypeConverter
import com.example.todocitas.data.local.entities.CitaState
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringToCitaState(value: String?): CitaState? {
        return value?.let { CitaState.valueOf(it) }
    }

    @TypeConverter
    fun citaStateToString(citaState: CitaState?): String? {
        return citaState?.name
    }
}