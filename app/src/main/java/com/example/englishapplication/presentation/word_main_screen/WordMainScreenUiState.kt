package com.example.englishapplication.presentation.word_main_screen

import com.example.englishapplication.domain.model.WordResponseWithDate

sealed interface WordMainScreenUiState {
    object Idle: WordMainScreenUiState
    object Loading: WordMainScreenUiState
    data class Success(val wordResponse: List<WordResponseWithDate>): WordMainScreenUiState
    data class Error(val message: String): WordMainScreenUiState

}