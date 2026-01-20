package com.example.todocitas.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocitas.data.local.entities.Servicio
import com.example.todocitas.data.local.repository.ServiciosRepository
import com.example.todocitas.states.ServiciosState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Indica que esta clase ViewModel se inyectará automáticamente
class ServiciosViewModel @Inject constructor (
    private val repository: ServiciosRepository // El repositorio se pasa como dependencia
) : ViewModel() {

    var serviciosState by mutableStateOf(ServiciosState())
    private set     // Solo el ViewModel puede modificar el estado

    init {
        getServicios()
    }

    fun getServicios() {
        viewModelScope.launch {
            repository.getServicios.collect {
                serviciosState = serviciosState.copy(
                    listaServicios = it
                )
            }
        }
    }

    fun getServicioById(id: Int) {
        viewModelScope.launch {
            repository.obtenerServicioById(id)
        }
    }

    fun guardarServicio(servicio: Servicio) {
        // Ejecuta la operación de guardado en una corutina
        viewModelScope.launch {
            repository.guardarServicio(servicio)
        }
    }

    fun actualizarServicio(servicio: Servicio) {
        viewModelScope.launch {
            repository.actualizarServicio(servicio)
        }
    }

    fun eliminarServicio(servicio: Servicio) {
        viewModelScope.launch {
            repository.eliminarServicio(servicio)
        }
    }
}