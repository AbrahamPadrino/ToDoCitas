package com.example.todocitas.data.local.repository

import com.example.todocitas.data.local.dao.ClientesDao
import com.example.todocitas.data.local.entities.Cliente
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientesRepository @Inject constructor(private val clientesDao: ClientesDao) {

    fun getClientes() : Flow<List<Cliente>> = clientesDao.getClientes()

    suspend fun getClienteById(id: Int) : Cliente? = clientesDao.getClienteById(id)

    suspend fun insertCliente(cliente: Cliente) = clientesDao.insertCliente(cliente)

    suspend fun updateCliente(cliente: Cliente) = clientesDao.updateCliente(cliente)

    suspend fun deleteCliente(cliente: Cliente) = clientesDao.deleteCliente(cliente)
}