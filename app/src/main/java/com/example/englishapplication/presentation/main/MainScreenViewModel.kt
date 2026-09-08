package com.example.englishapplication.presentation.main

import androidx.lifecycle.ViewModel
import com.example.englishapplication.util.NavigationEvent
import com.example.englishapplication.util.NavigationEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val navigationEventManager: NavigationEventManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainScreenUiState>(MainScreenUiState.Idle)
    val uiState : StateFlow<MainScreenUiState> = _uiState

    val navigationEvents : StateFlow<NavigationEvent?> = navigationEventManager.events

    fun onNavigationHandled() {
        navigationEventManager.consumeEvent()
    }
}