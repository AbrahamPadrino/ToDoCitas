package com.example.todocitas.states

import com.example.todocitas.data.local.entities.Cliente

data class ClientesState(
    var listaClientes: List<Cliente> = emptyList()
)
