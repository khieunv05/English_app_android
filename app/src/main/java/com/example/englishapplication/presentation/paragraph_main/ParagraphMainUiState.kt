package com.example.englishapplication.presentation.paragraph_main

import com.example.englishapplication.domain.model.PhraseResponse

sealed interface ParagraphMainUiState {
    object Idle: ParagraphMainUiState
    object Loading: ParagraphMainUiState
    object Success: ParagraphMainUiState
    data class Error(val msg: String): ParagraphMainUiState
}