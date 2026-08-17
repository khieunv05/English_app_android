package com.example.englishapplication.presentation.add_word

sealed interface AddWordUiState {
    object Idle: AddWordUiState
    object Loading: AddWordUiState
    data class Success(val message: String): AddWordUiState
    data class Error(val message: String): AddWordUiState
}