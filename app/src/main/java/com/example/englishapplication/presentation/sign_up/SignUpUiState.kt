package com.example.englishapplication.presentation.sign_up

sealed interface SignUpUiState {
    object Idle: SignUpUiState
    object Loading: SignUpUiState
    data class Success(val message: String): SignUpUiState
    data class Error (val message: String) : SignUpUiState
}