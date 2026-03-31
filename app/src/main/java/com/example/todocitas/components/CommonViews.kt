package com.example.todocitas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todocitas.ui.theme.BorderDark
import com.example.todocitas.ui.theme.CardDark
import com.example.todocitas.ui.theme.Primary
import com.example.todocitas.ui.theme.TextSecondary

// Un Composable reutilizable para los campos de texto personalizados
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    singleLine: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default, // Usamos .Default para consistencia
    isError: Boolean = false,
    errorMessage: String? = null

) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = if (isError) Color.Red else Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextSecondary) },
            leadingIcon = leadingIcon,
            shape = RoundedCornerShape(12.dp),
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError, // <--- Propiedad nativa de Material3
            supportingText = { // <--- Texto debajo del campo
                if (isError && errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = CardDark,
                focusedContainerColor = CardDark,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Primary,
                unfocusedBorderColor = BorderDark,
                // Colores específicos para el estado de error
                errorBorderColor = Color.Red,
                errorLeadingIconColor = Color.Red,
                errorCursorColor = Color.Red
            )
        )
    }
}

// Un Composable reutilizable para la barra de búsqueda
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.height(52.dp),
        placeholder = { Text("Buscar cliente...", color = TextSecondary) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = TextSecondary
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CardDark,
            unfocusedContainerColor = CardDark,
            disabledContainerColor = CardDark,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

/**
 * Componente reutilizable
 */
// Controles de Paginación
@Composable
fun PaginacionControles(
    paginaActual: Int, totalPaginas: Int,
    onCambiarPagina: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Ir a la página ANTERIOR
        IconButton(
            onClick = { onCambiarPagina(paginaActual - 1) },
            // El botón se deshabilita si está en la primera página
            enabled = paginaActual > 1
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Página anterior",
                // El color cambia si el botón está deshabilitado para dar feedback visual
                tint = if (paginaActual > 1) Primary else TextSecondary.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.width(24.dp))
        // Texto que indica la página actual y el total
        Text(
            text = "Página $paginaActual de $totalPaginas",
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(24.dp))
        // Botón para ir a la página SIGUIENTE
        IconButton(
            onClick = { onCambiarPagina(paginaActual + 1) },
            // El botón se deshabilita si está en la última página
            enabled = paginaActual < totalPaginas
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Página siguiente",
                tint = if (paginaActual < totalPaginas) Primary else TextSecondary.copy(alpha = 0.5f)
            )
        }
    }
}

// Lista sin mas registos
@Composable
fun EmptyState(isEndOfList: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isEndOfList) 60.dp else 80.dp)
                .clip(CircleShape)
                .background(CardDark),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.BackHand,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(if (isEndOfList) 30.dp else 40.dp)
            )
        }
        Text(
            text = "No hay registros disponibles.",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 18.sp
        )
        Text(
            text = "Presiona '+' para agregar un nuevo registro.",
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}