package com.example.todocitas.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocitas.data.local.room.ClientesDao
import com.example.todocitas.models.Cliente
import com.example.todocitas.states.ClientesState
import kotlinx.coroutines.launch

class ClientesViewModel(
    private val clientesDao: ClientesDao
) : ViewModel() {
    var clientesState by mutableStateOf(ClientesState())
        private set     // Solo el ViewModel puede modificar el estado

    init {
        viewModelScope.launch {
            clientesDao.getClientes().collect {
                clientesState = clientesState.copy(listaClientes = it) }
        }
    }

    fun agregarCliente(cliente: Cliente) {
        viewModelScope.launch {
            clientesDao.insertCliente(cliente)
        }
    }

    fun actualizarCliente(cliente: Cliente) {
        viewModelScope.launch {
            clientesDao.updateCliente(cliente)
        }
    }

    fun eliminarCliente(cliente: Cliente) {
        viewModelScope.launch {
            clientesDao.deleteCliente(cliente)
        }
    }
}