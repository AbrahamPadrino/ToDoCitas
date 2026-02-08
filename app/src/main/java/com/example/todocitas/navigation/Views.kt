package com.example.todocitas.navigation

sealed class Views(
    val route: String
) {
    object InicioView: Views("inicio_view")
    object ListaClientesView: Views("lista_clientes_view")
    object NuevoClienteView: Views("nuevo_cliente_view")
    object ListaServiciosView: Views("lista_servicios_view")
    object NuevoServicioView: Views("nuevo_servicio_view")
    object ListaCitasView: Views("lista_citas_view")
    object NuevaCitaView: Views("nueva_cita_view")
    object ReporteTicketView: Views("reporte_ticket_view")
    object ReporteSemanalView: Views("reporte_semanal_view")
    object ReporteMensualView: Views("reporte_mensual_view")
}