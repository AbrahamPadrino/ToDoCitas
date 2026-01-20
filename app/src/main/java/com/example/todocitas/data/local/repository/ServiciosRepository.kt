package com.example.todocitas.data.local.repository

import com.example.todocitas.data.local.entities.Servicio
import com.example.todocitas.data.local.dao.ServiciosDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Repositorio para operaciones relacionadas con Servicios
class ServiciosRepository @Inject constructor(private val serviciosDao: ServiciosDao) {

    // Expone el Flow directamente desde el DAO. Es la forma más óptima y reactiva.
    val getServicios: Flow<List<Servicio>> = serviciosDao.getAllServicios()

    suspend fun obtenerServicioById(id: Int): Servicio? {
        return serviciosDao.getServicioById(id)
    }

    // Las operaciones de escritura se delegan al DAO dentro de funciones suspend.
    suspend fun guardarServicio(servicio: Servicio) {
        serviciosDao.insertServicio(servicio)
    }

    suspend fun actualizarServicio(servicio: Servicio) {
        serviciosDao.updateServicio(servicio)
    }

    suspend fun eliminarServicio(servicio: Servicio) {
        serviciosDao.deleteServicio(servicio)
    }
}