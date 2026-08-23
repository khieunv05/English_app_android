package com.example.englishapplication.presentation.main

sealed interface MainScreenUiState {
    object Loading : MainScreenUiState

    object Idle: MainScreenUiState
}