/**
 * Análisis de CitasRepository:
 * Esta clase actúa como un intermediario entre el ViewModel y el DAO.
 * Guarda una cita completa. Esta operación es transaccional por naturaleza.
 * 1. Inserta la entidad Cita.
 * 2. Obtiene el ID de la nueva cita.
 * 3. Itera sobre la lista de servicios y crea una entrada en la tabla de unión
 *    por cada servicio seleccionado.
 *
 * @param clienteId El ID del cliente para la cita.
 * @param fecha La fecha y hora de la cita.
 * @param serviciosSeleccionados La lista de servicios que el cliente ha elegido.
 */

package com.example.todocitas.data.local.repository

import com.example.todocitas.data.local.dao.CitasDao
import com.example.todocitas.data.local.entities.Cita
import com.example.todocitas.data.local.entities.CitaServicioCrossRef
import com.example.todocitas.data.local.entities.CitaState
import com.example.todocitas.data.local.entities.Servicio
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class CitasRepository @Inject constructor(private val citasDao: CitasDao) {

    suspend fun crearNuevaCita(clienteId: Int, fecha: Date, serviciosSeleccionados: List<Servicio>) {
        // 1. Calcular el precio total a partir de la lista de servicios.
        val precioTotal = serviciosSeleccionados.sumOf { it.precio }

        // 2. Crear la entidad Cita principal.
        val nuevaCita = Cita(
            clienteId = clienteId,
            fecha = fecha,
            precioTotal = precioTotal,
            estado = CitaState.PENDIENTE // Por defecto, la cita está pendiente.
        )

        // 3. Insertar la cita en la base de datos y obtener su ID.
        val citaId = citasDao.insertCita(nuevaCita)

        // 4. Crear y guardar las referencias cruzadas en la tabla de unión.
        serviciosSeleccionados.forEach { servicio ->
            val crossRef = CitaServicioCrossRef(
                citaId = citaId.toInt(),
                servicioId = servicio.id
            )
            citasDao.insertCitaServicioCrossRef(crossRef)
        }
    }

    /**
     * Obtiene los detalles completos de una cita, incluyendo sus servicios.
     * Ideal para mostrar el "ticket" o la vista de detalles de una cita.
     */
    suspend fun obtenerCitaConServicios(citaId: Int) = citasDao.getCitaConServicios(citaId)

    /**
     * Expone el Flow para obtener los reportes de ganancias por un período.
     * El ViewModel simplemente pasará las fechas de inicio y fin.
     */
    fun obtenerGananciasPorPeriodo(fechaInicio: Date, fechaFin: Date): Flow<Double?> {
        return citasDao.getGananciasPorPeriodo(fechaInicio, fechaFin)
    }

    /**
     * Actualiza el estado de una cita.
     * Esto podría ser útil para cambiar el estado de una cita desde la UI.
     * */
    suspend fun actualizarEstadoCita(citaId: Int, nuevoEstado: CitaState) {
        citasDao.updateCitaState(citaId, nuevoEstado)
    }
    /** Lógica para eliminar la cita de la base de datos.
     * Asegúrate de eliminar las referencias cruzadas en la tabla de unión.
     * Luego, elimina la cita principal.
     * */
    suspend fun eliminarCita(citaId: Int) {
        citasDao.deleteCita(citaId)
    }
}