package com.example.todocitas.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BackgroundDark
            ) {
                DrawerContent(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Evita apilar la misma pantalla múltiples veces
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

        val serviciosViewModel: ServiciosViewModel = hiltViewModel()
        val clieneteViewModel: ClientesViewModel = hiltViewModel()

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
                    clientes = clieneteViewModel.clientesState.listaClientes,
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }
            composable(Views.NuevoClienteView.route) {
                NuevoClienteView(
                    onBack = { navController.popBackStack() },
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } },

                    //onSaveCliente = { clieneteViewModel.guardarCliente(it) }

                )
            }

            composable(Views.ListaServiciosView.route) {
                ListaServiciosView(
                    onBack = { navController.popBackStack() },
                    onAddNewService = { navController.navigate(Views.NuevoServicioView.route) },
                    servicios = serviciosViewModel.serviciosState.listaServicios,
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Views.NuevoServicioView.route) {
                NuevoServicioView(
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } },
                    onBack = { navController.popBackStack() },
                    onSaveService = { serviciosViewModel.guardarServicio(it) }
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