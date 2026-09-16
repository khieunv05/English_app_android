package com.example.englishapplication.presentation.profile

sealed interface ProfileUiState {
    object Idle : ProfileUiState

    object LoggingOut : ProfileUiState

    object LoggedOut : ProfileUiState
}
