package com.example.todocitas.states

import com.example.todocitas.data.local.entities.Servicio

data class ServiciosState(
    val listaServicios: List<Servicio> = emptyList()
)