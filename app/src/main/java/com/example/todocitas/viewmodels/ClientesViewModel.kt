package com.example.todocitas.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocitas.data.local.entities.Cliente
import com.example.todocitas.data.local.repository.ClientesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class   ClientesViewModel @Inject constructor(
    private val repository: ClientesRepository
) : ViewModel() {
    //
    val clientesState: StateFlow<List<Cliente>> = repository.getClientes()
        .stateIn(
            scope = viewModelScope, // La corutina vive mientras el ViewModel viva.
            started = SharingStarted.WhileSubscribed(5000L), // El flujo se mantiene activo 5s después de que la UI deja de observar.
            initialValue = emptyList() // El valor inicial mientras se cargan los datos.
        )

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