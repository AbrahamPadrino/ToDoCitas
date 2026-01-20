package com.example.todocitas.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocitas.data.local.entities.CitaState
import com.example.todocitas.data.local.entities.Servicio
import com.example.todocitas.data.local.repository.CitasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CitasViewModel @Inject constructor(
    private val repository: CitasRepository
) : ViewModel() {

    // StateFlow para las ganancias del reporte, por ejemplo, mensual y semanal.
    private val _gananciasMensuales = MutableStateFlow<Double?>(0.0)
    val gananciasMensuales = _gananciasMensuales.asStateFlow()

    private val _gananciasSemanales = MutableStateFlow<Double?>(0.0)
    val gananciasSemanales = _gananciasSemanales.asStateFlow()
    //

    /**
     * Orquesta la creación de una nueva cita.
     * Llama al repositorio para guardar la cita y sus servicios asociados.
     */
    fun crearNuevaCita(clienteId: Int, fecha: Date, servicios: List<Servicio>) {
        // Validaciones básicas (puedes añadir más)
        if (clienteId <= 0 || servicios.isEmpty()) {
            // Manejar error (ej. mostrar un Toast, un Snackbar, o actualizar un estado de error)
            return
        }

        viewModelScope.launch {
            repository.crearNuevaCita(clienteId, fecha, servicios)
            // Opcional: podrías emitir un evento de "éxito" aquí.
        }
    }

    /**
    * Obtiene los detalles completos de una cita, incluyendo sus servicios.
    * Ideal para mostrar el "ticket" o la vista de detalles de una cita.
    */
    suspend fun obtenerCitaConServicios(citaId: Int) = repository.obtenerCitaConServicios(citaId)

    /**
     * Calcula y obtiene las ganancias para el mes actual.
     * Esta función puede ser llamada desde la vista de reportes.
     */
    fun cargarGananciasDelMesActual() {
        val calendar = Calendar.getInstance()

        // Establecer el inicio del mes
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val fechaInicio = calendar.time

        // Establecer el fin del mes
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val fechaFin = calendar.time

        viewModelScope.launch {
            repository.obtenerGananciasPorPeriodo(fechaInicio, fechaFin).collect { ganancias ->
                _gananciasMensuales.value = ganancias
            }
        }
    }

    /**
     * Calcula y obtiene las ganancias para la semana actual.
     * Esta función puede ser llamada desde la vista de reportes.
     */
    fun cargarGananciasDeLaSemana() {
        val calendar = Calendar.getInstance()
        // Establecer inicio de la semana
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val fechaInicio = calendar.time

        // Establecer fin de la semana
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val fechaFin = calendar.time

        viewModelScope.launch {
            repository.obtenerGananciasPorPeriodo(fechaInicio, fechaFin).collect { ganancias ->
                _gananciasSemanales.value = ganancias
            }
        }

    }

    /**
     * Actualiza el estado de una cita.
     * Esto podría ser útil para cambiar el estado de una cita desde la UI.
     */
    fun actualizarEstadoCita(citaId: Int, nuevoEstado: CitaState) {
        viewModelScope.launch {
            repository.actualizarEstadoCita(citaId, nuevoEstado)
        }
    }

    /** Lógica para eliminar la cita de la base de datos.
     * Asegúrate de eliminar las referencias cruzadas en la tabla de unión.
     * Luego, elimina la cita principal.
     */
    fun eliminarCita(citaId: Int) {
        viewModelScope.launch {
            repository.eliminarCita(citaId)
        }
    }
}

/*
* Resumen y Próximos Pasos

1.  CitasRepository: Has creado una capa de abstracción robusta. Su responsabilidad es clara: comunicarse con el CitasDao y contener la lógica para operaciones complejas como crearNuevaCita, que involucra múltiples pasos en la base de datos.
2.  CitasViewModel: Este ViewModel sirve como el cerebro para tu UI. Expone métodos simples como crearCita() y cargarGananciasDelMesActual(). Las vistas de Compose no necesitarán saber nada sobre la base de datos o el repositorio; simplemente llamarán a estas funciones.
3.  Inyección de Dependencias: Gracias a que ya configuraste Hilt y el AppModule, estas nuevas clases se inyectarán automáticamente donde las necesites. Hilt sabe cómo crear CitasRepository porque le has enseñado a proveer CitasDao. Y sabe cómo crear CitasViewModel porque has anotado su constructor con @Inject.

* Tu próximo paso será utilizar este CitasViewModel en tus pantallas de Compose, por ejemplo:
• En una nueva pantalla "Nueva Cita", llamarías a citasViewModel.crearCita(...).
• En una pantalla de "Reportes", podrías observar el StateFlow gananciasMensuales para mostrar el total en la UI.
* */