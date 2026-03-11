package com.example.todocitas.utils

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

/**
 * Valida que una lista de pares (valor, nombre_campo) no contengan strings vacíos.
 * @param fields Una lista de Pair donde el primero es el valor y el segundo el nombre para el error.
 */
object FormValidator {
    fun validateEmptyFields(vararg fields: Pair<String, String>): ValidationResult {
        for ((value, label) in fields) {
            if (value.trim().isEmpty()) {
                return ValidationResult.Error("El campo '$label' es obligatorio")
            }
        }
        return ValidationResult.Success
    }
}