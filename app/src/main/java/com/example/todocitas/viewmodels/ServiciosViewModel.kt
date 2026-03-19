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

    /** Inicio de la logica de manejo de validación de campos*/
    // Estado para los errores
    var errorState by mutableStateOf(ServicioFormErrorState())
        private set
    // Función para validar
    fun validarCampos(nombre: String, precio: String, descripcion: String): Boolean {
        val nombreEsValido = nombre.isNotBlank()
        val precioEsValido = precio.isNotBlank() && precio.toDoubleOrNull() != null
        val descripcionEsValida = descripcion.isNotBlank()


        errorState = ServicioFormErrorState(
            nombreError = if (nombreEsValido) null else "El nombre no puede estar vacío",
            precioError = if (precioEsValido) null else "Ingrese un precio válido",
            descripcionError = if (descripcionEsValida) null else "La descripción no puede estar vacía"
        )

        return nombreEsValido && precioEsValido && descripcionEsValida
    }

    // Función para limpiar el error de un campo cuando el usuario vuelve a escribir
    fun onNombreChange() {
        if (errorState.nombreError != null) {
            errorState = errorState.copy(nombreError = null)
        }
    }

    fun onPrecioChange() {
        if (errorState.precioError != null) {
            errorState = errorState.copy(precioError = null)
        }
    }
    fun onDescripcionChange() {
        if (errorState.descripcionError != null) {
            errorState = errorState.copy(descripcionError = null)
        }
    }
    /** Fin de la logica de manejo de validación de campos*/

    // Estado para la lista de servicios
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

    suspend fun getServicioById(id: Int): Servicio? {
        return repository.getServicioById(id)
    }

    fun agregarServicio(servicio: Servicio) {
        // Ejecuta la operación de guardado en una corutina
        viewModelScope.launch {
            repository.insertServicio(servicio)
        }
    }

    fun updateServicio(servicio: Servicio) {
        viewModelScope.launch {
            repository.updateServicio(servicio)
        }
    }

    fun eliminarServicio(servicio: Servicio) {
        viewModelScope.launch {
            repository.deleteServicio(servicio)
        }
    }
}

// Data class para representar los errores del formulario
data class ServicioFormErrorState(
    val nombreError: String? = null,
    val precioError: String? = null,
    val descripcionError: String? = null
)