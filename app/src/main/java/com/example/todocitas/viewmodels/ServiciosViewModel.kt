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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

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

    // Estados para la paginación
    private val _paginaActual = MutableStateFlow(1)
    val paginaActual = _paginaActual.asStateFlow()

    private val _totalPaginas = MutableStateFlow(1)
    // Función para el Nombre a buscar
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val registrosPorPagina = 10

    // Total de páginas reacciona al "searchQuery".
    @OptIn(ExperimentalCoroutinesApi::class)
    val totalPaginas: StateFlow<Int> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.countServicios()
        } else {
            repository.countSearchResults(query)
        }
    }.map { totalServicios ->
        ceil(totalServicios.toDouble() / registrosPorPagina).toInt().coerceAtLeast(1)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    // La Lista de Servicios ahora reacciona tanto a "paginaActual" como a "searchQuery".
    @OptIn(ExperimentalCoroutinesApi::class)
    val serviciosPaginados: StateFlow<List<Servicio>> = combine(_paginaActual,
        _searchQuery
    ) { pagina, query ->
        Pair(pagina, query)
    }.flatMapLatest { (pagina, query) ->
        val offset = (pagina - 1) * registrosPorPagina
        if (query.isBlank()) {
            repository.getServiciosPaginados(registrosPorPagina, offset)
        } else {
            repository.searchAndPaginateServicios(query, registrosPorPagina, offset)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    init {
        // Observa el conteo total de servicios para calcular el número de páginas.
        viewModelScope.launch {
            repository.countServicios().collect { totalServicios ->
                _totalPaginas.value = ceil(totalServicios.toDouble() / registrosPorPagina).toInt().coerceAtLeast(1)
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        // Reinicia a la página 1 cada vez que la búsqueda cambia.
        _paginaActual.value = 1
    }

    fun cambiarPagina(nuevaPagina: Int) {
        // Asegura que la nueva página esté dentro de los límites.
        if (nuevaPagina >= 1 && nuevaPagina <= _totalPaginas.value) {
            _paginaActual.value = nuevaPagina
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