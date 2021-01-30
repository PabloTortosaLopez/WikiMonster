package com.example.wikimonster.ui.imc

/**
 * Validación de datos para el formulario de peso y altura.
 */
data class IMCFormState(val weightError: Int? = null,
                        val heightError: Int? = null,
                        val isDataValid: Boolean = false)