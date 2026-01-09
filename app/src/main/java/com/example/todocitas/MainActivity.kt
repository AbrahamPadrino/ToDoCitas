package com.example.todocitas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.todocitas.ui.theme.ToDoCitasTheme
import com.example.todocitas.views.InicioView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // --- Control de la Barra de Estado ---
            // 1. Obtenemos el controlador de la UI del sistema
            val systemUiController = rememberSystemUiController()
            // 2. Determinamos si se debe usar el tema oscuro (puedes forzarlo a true si siempre es oscuro)
            val useDarkIcons = !isSystemInDarkTheme()
            // 3. Usamos LaunchedEffect para que el cambio se aplique una sola vez
            LaunchedEffect(systemUiController, useDarkIcons) {
                // Hacemos la barra de estado transparente para que el contenido se dibuje detrás
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = false // <-- Los íconos en blanco.
                )
            }
            //
            ToDoCitasTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // --- Aquí llamas a tu vista principal ---
                    InicioView()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoCitasTheme {
        //
    }
}
