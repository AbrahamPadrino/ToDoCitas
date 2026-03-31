package com.example.todocitas.viewmodels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocitas.data.local.entities.Cliente
import com.example.todocitas.data.local.repository.ClientesRepository
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
import android.util.Patterns.EMAIL_ADDRESS
import android.util.Patterns.PHONE

@HiltViewModel
class   ClientesViewModel @Inject constructor(
    private val repository: ClientesRepository
) : ViewModel() {

    /** Inicio de la logica de manejo de validación de campos*/
    // Estado para los errores
    var errorState by mutableStateOf(ClienteFormErrorState())
        private set

    // Función para validar
    fun validarCampos(nombre: String, apellido: String, correo: String, telefono: String): Boolean {
        val nombreEsValido = nombre.isNotBlank()
        val apellidoEsValido = apellido.isNotBlank()
        val correoEsValido = correo.isNotBlank() && EMAIL_ADDRESS.matcher(correo).matches()
        val telefonoEsValido = telefono.isNotBlank() && PHONE.matcher(telefono).matches()

        errorState = ClienteFormErrorState(
            nombreError = if (nombreEsValido) null else "El nombre no puede estar vacío",
            apellidoError = if (apellidoEsValido) null else "El apellido no puede estar vacío",
            correoError = if (correoEsValido) null else "Ingrese un correo válido",
            telefonoError = if (telefonoEsValido) null else "Ingrese un número de teléfono válido"
        )

        return nombreEsValido && apellidoEsValido && correoEsValido && telefonoEsValido
    }
    // Función para limpiar el error de un campo cuando el usuario vuelve a escribir
    fun onNombreChange() {
        if (errorState.nombreError != null) {
            errorState = errorState.copy(nombreError = null)
        }
    }
    fun onApellidoChange() {
        if (errorState.apellidoError != null) {
            errorState = errorState.copy(apellidoError = null)
        }
    }
    fun onCorreoChange() {
        if (errorState.correoError != null) {
            errorState = errorState.copy(correoError = null)
        }
    }
    fun onTelefonoChange() {
        if (errorState.telefonoError != null) {
            errorState = errorState.copy(telefonoError = null)
        }
    }
    /** Fin de la logica de manejo de validación de campos*/


    private val _paginaActual = MutableStateFlow(1)
    val paginaActual = _paginaActual.asStateFlow()

    private val _totalPaginas = MutableStateFlow(1)

    // Función para el Nombre a buscar
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Registros por página es una constante.
    private val registrosPorPagina = 10

    // Total de páginas ahora reacciona al "searchQuery".
    @OptIn(ExperimentalCoroutinesApi::class)
    val totalPaginas: StateFlow<Int> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.countClientes()
        } else {
            repository.countSearchResults(query)
        }
    }.map { totalClientes ->
        ceil(totalClientes.toDouble() / registrosPorPagina).toInt().coerceAtLeast(1)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    // La Lista de Clientes ahora reacciona tanto a "paginaActual" como a "searchQuery".
    @OptIn(ExperimentalCoroutinesApi::class)
    val clientesPaginados: StateFlow<List<Cliente>> = combine(
        _paginaActual,
        _searchQuery
    ) { pagina, query ->
        Pair(pagina, query)
    }.flatMapLatest { (pagina, query) ->
        val offset = (pagina - 1) * registrosPorPagina
        if (query.isBlank()) {
            repository.getClientesPaginados(registrosPorPagina, offset)
        } else {
            repository.searchAndPaginateClientes(query, registrosPorPagina, offset)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Observa el conteo total de clientes para calcular el número de páginas.
        viewModelScope.launch {
            repository.countClientes().collect { totalClientes ->
                _totalPaginas.value = ceil(totalClientes.toDouble() / registrosPorPagina).toInt().coerceAtLeast(1)
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

    suspend fun getClienteById(id: Int): Cliente? {
            return repository.getClienteById(id)
    }

    fun agregarCliente(cliente: Cliente) {
        viewModelScope.launch {
            repository.insertCliente(cliente)
        }
    }

    fun updateCliente(cliente: Cliente) {
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

// Data class para representar los errores del formulario
data class ClienteFormErrorState(
    val nombreError: String? = null,
    val apellidoError: String? = null,
    val correoError: String? = null,
    val telefonoError: String? = null
)