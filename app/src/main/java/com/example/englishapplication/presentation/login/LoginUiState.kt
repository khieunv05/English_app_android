package com.example.englishapplication.presentation.login

sealed interface LoginUiState {
    object Idle: LoginUiState
    object Loading: LoginUiState
    object Success: LoginUiState
    data class Error(var message: String): LoginUiState
}