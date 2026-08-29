package com.example.englishapplication.presentation.paragraph_add

sealed interface AddParagraphUiState {
    object Idle: AddParagraphUiState
    object Loading: AddParagraphUiState
    data class Success(val msg: String): AddParagraphUiState
    data class Error(val msg: String): AddParagraphUiState
    object FindSuccess: AddParagraphUiState
}