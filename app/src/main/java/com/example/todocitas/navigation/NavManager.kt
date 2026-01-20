package com.example.todocitas.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todocitas.viewmodels.ClientesViewModel
import com.example.todocitas.viewmodels.ServiciosViewModel
import com.example.todocitas.views.InicioView
import com.example.todocitas.views.ListaClientesView
import com.example.todocitas.views.ListaServiciosView
import com.example.todocitas.views.NuevoClienteView
import com.example.todocitas.views.NuevoServicioView


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun NavManager() {

    val navController = rememberNavController()

    val serviciosViewModel: ServiciosViewModel = hiltViewModel()
    val clieneteViewModel: ClientesViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Views.InicioView.route
    ) {
        composable(Views.InicioView.route) {
            InicioView(navController = navController)
        }

        composable(Views.ListaClientesView.route) {
            ListaClientesView(
                onBack = { navController.popBackStack() },
                onAddNewClient = { navController.navigate(Views.NuevoClienteView.route) },
                clientes = clieneteViewModel.clientesState.listaClientes,
                navController = navController
            )
        }

        composable(Views.ListaServiciosView.route) {

            ListaServiciosView(
                onBack = { navController.popBackStack() },
                onAddNewService = { navController.navigate(Views.NuevoServicioView.route) },
                servicios = serviciosViewModel.serviciosState.listaServicios,
                navController = navController)
        }

        composable(Views.NuevoClienteView.route) {
            NuevoClienteView(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }

        composable(Views.NuevoServicioView.route) {
            NuevoServicioView(
                navController = navController,
                onBack = { navController.popBackStack() },
                onSaveService = { serviciosViewModel.guardarServicio(it) }
            )
        }
    }
}