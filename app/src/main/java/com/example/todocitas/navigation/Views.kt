package com.example.todocitas.navigation

sealed class Views(
    val route: String
) {
    object InicioView: Views("inicio_view")
    object ListaClientesView: Views("lista_clientes_view")
    object ListaServiciosView: Views("lista_servicios_view")
    object NuevoClienteView: Views("nuevo_cliente_view")
    object NuevoServicioView: Views("nuevo_servicio_view")

}