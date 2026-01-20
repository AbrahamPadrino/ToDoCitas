package com.example.todocitas.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocitas.data.local.entities.Cliente
import com.example.todocitas.data.local.repository.ClientesRepository
import com.example.todocitas.states.ClientesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientesViewModel @Inject constructor(
    private val repository: ClientesRepository
) : ViewModel() {
    var clientesState by mutableStateOf(ClientesState())
        private set     // Solo el ViewModel puede modificar el estado

    init {
        getClientes()
    }
    fun getClientes() {
        viewModelScope.launch {
            repository.getClientes().collect {
                clientesState = clientesState.copy(
                    listaClientes = it
                )
            }
        }
    }

    fun getClienteById(id: Int) {
        viewModelScope.launch {
            repository.getClienteById(id)
        }
    }

    fun agregarCliente(cliente: Cliente) {
        viewModelScope.launch {
            repository.insertCliente(cliente)
        }
    }

    fun actualizarCliente(cliente: Cliente) {
        viewModelScope.launch {
            repository.updateCliente(cliente)
        }
    }

    fun eliminarCliente(cliente: Cliente) {
        viewModelScope.launch {
            repository.deleteCliente(cliente)

        }
    }
}