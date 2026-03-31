package com.example.todocitas.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todocitas.ui.theme.BackgroundDark
import com.example.todocitas.viewmodels.ClientesViewModel
import com.example.todocitas.viewmodels.ServiciosViewModel
import com.example.todocitas.views.InicioView
import com.example.todocitas.views.ListaCitasView
import com.example.todocitas.views.ListaClientesView
import com.example.todocitas.views.ListaServiciosView
import com.example.todocitas.views.NuevaCitaView
import com.example.todocitas.views.ReporteTicketView
import com.example.todocitas.views.NuevoClienteView
import com.example.todocitas.views.NuevoServicioView
import com.example.todocitas.views.ReporteMensualView
import com.example.todocitas.views.ReporteSemanalView
import com.example.todocitas.views.components.DrawerContent
import kotlinx.coroutines.launch


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun NavManager() {
    // ---- Estado para el Navigation Drawer ----
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    // Para saber en qué ruta estamos y resaltar el item correcto del menú
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Views.InicioView.route

    val serviciosViewModel: ServiciosViewModel = hiltViewModel()
    val clientesViewModel: ClientesViewModel = hiltViewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BackgroundDark
            ) {
                DrawerContent(
                    currentRoute = currentRoute,
                    onNavigate = { route ->

                        // Resetea la página actual de Clientes al pulsar un item del menú
                        if (route == Views.ListaClientesView.route) {
                            clientesViewModel.cambiarPagina(1)
                        }
                        // Resetea la página actual de Servicios al pulsar un item del menú
                        if (route == Views.ListaServiciosView.route)
                            serviciosViewModel.cambiarPagina(1)
                        // Evita apilar la misma pantalla múltiples veces
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                        // Cierra el menú después de navegar
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onLogout = { /* Lógica para cerrar sesión */ }
                )
            }
        },
        gesturesEnabled = drawerState.isOpen // Solo permite gestos para abrir/cerrar si está abierto
    ) {
        // Recoge la lista de clientes paginada
        val listaDeClientesPaginada by clientesViewModel.clientesPaginados.collectAsState()
        // Recoge la información de paginación
        val paginaActualClientes by clientesViewModel.paginaActual.collectAsState()
        val totalPaginasClientes by clientesViewModel.totalPaginas.collectAsState()
        val searchQueryClientes by clientesViewModel.searchQuery.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Views.InicioView.route
        ) {
            composable(Views.InicioView.route) {
                // Pasamos el scope y el drawerState para poder abrir el menú
                InicioView(
                    navController = navController,
                    openDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            composable(Views.ListaClientesView.route) {
                ListaClientesView(
                    onBack = { navController.popBackStack() },
                    onAddNewClient = { navController.navigate(Views.NuevoClienteView.route) },
                    clientes = listaDeClientesPaginada,
                    paginaActual = paginaActualClientes,
                    totalPaginas = totalPaginasClientes,
                    onCambiarPagina = { nuevaPagina ->
                        clientesViewModel.cambiarPagina(nuevaPagina)
                    },
                    searchQuery = searchQueryClientes,
                    onSearchQueryChange = { newQuery ->
                        clientesViewModel.onSearchQueryChange(
                            newQuery
                        )
                    },
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } },
                    onEditCliente = { cliente ->
                        // NAVEGA A LA RUTA DE EDICIÓN PASANDO EL ID
                        navController.navigate(Views.NuevoClienteView.route + "?clienteId=${cliente.id}")
                    },
                    onDeleteCliente = { cliente -> clientesViewModel.eliminarCliente(cliente) }
                )
            }

            composable(Views.ListaServiciosView.route) {
                // 1. Estados específicos de Servicios
                val serviciosPaginados by serviciosViewModel.serviciosPaginados.collectAsState()
                val paginaActualServicios by serviciosViewModel.paginaActual.collectAsState()
                val totalPaginasServicios by serviciosViewModel.totalPaginas.collectAsState()
                val searchServicios by serviciosViewModel.searchQuery.collectAsState()

                ListaServiciosView(
                    onBack = { navController.popBackStack() },
                    onAddNewService = { navController.navigate(Views.NuevoServicioView.route) },
                    servicios = serviciosPaginados,
                    // 2. Parámetros reseteados al contexto de servicios
                    paginaActual = paginaActualServicios,
                    totalPaginas = totalPaginasServicios,
                    onCambiarPagina = { nuevaPagina ->
                        serviciosViewModel.cambiarPagina(nuevaPagina)
                    },
                    searchQuery = searchServicios,
                    onSearchQueryChange = { newQuery ->
                        serviciosViewModel.onSearchQueryChange(
                            newQuery
                        )
                    },
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } },
                    onEditServicio = { servicio ->
                        navController.navigate(Views.NuevoServicioView.route + "?servicioId=${servicio.id}")
                    },
                    onDeleteServicio = { servicio -> serviciosViewModel.eliminarServicio(servicio) }
                )
            }

            // Se añade un argumento opcional "clienteId". Si no se pasa, es una creación. Si se pasa, es una edición.
            composable(
                route = Views.NuevoClienteView.route + "?clienteId={clienteId}",
                arguments = listOf(navArgument("clienteId") {
                    type = NavType.IntType
                    defaultValue = -1 // Un valor por defecto que indique que no se está editando
                })
            ) { backStackEntry ->
                // Obtiene el ID de los argumentos de la ruta
                val clienteId = backStackEntry.arguments?.getInt("clienteId") ?: -1

                NuevoClienteView(
                    onBack = { navController.popBackStack() },
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } },
                    clienteId = clienteId, // Pasa el ID a la vista
                    clientesViewModel = clientesViewModel, // Pasa el ViewModel completo
                    onSaveComplete = { navController.popBackStack() }
                )
            }

            composable(
                route = Views.NuevoServicioView.route + "?servicioId={servicioId}",
                arguments = listOf(navArgument("servicioId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val servicioId = backStackEntry.arguments?.getInt("servicioId") ?: -1

                NuevoServicioView(
                    servicioId = servicioId,
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } },
                    onBack = { navController.popBackStack() },
                    onSaveService = { serviciosViewModel.agregarServicio(it) },
                    onSaveComplete = { navController.popBackStack() },
                    serviciosViewModel = serviciosViewModel
                )
            }

            composable(Views.ListaCitasView.route) {
                ListaCitasView(
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Views.NuevaCitaView.route) {
                NuevaCitaView(
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Views.ReporteTicketView.route) {
                ReporteTicketView(
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Views.ReporteSemanalView.route) {
                ReporteSemanalView(
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Views.ReporteMensualView.route) {
                ReporteMensualView(
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}