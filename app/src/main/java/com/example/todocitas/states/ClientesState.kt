package com.example.todocitas.states

import com.example.todocitas.models.Cliente

data class ClientesState(
    var listaClientes: List<Cliente> = emptyList()
)
