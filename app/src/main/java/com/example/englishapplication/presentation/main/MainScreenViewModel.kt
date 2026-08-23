package com.example.englishapplication.presentation.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<MainScreenUiState>(MainScreenUiState.Idle)
    val uiState : StateFlow<MainScreenUiState> = _uiState

    private val _selectedTab = MutableStateFlow(MainScreenTabs.HOME)

    val selectedTab : StateFlow<MainScreenTabs> = _selectedTab

    fun onChangeSelectedTab(newTab : MainScreenTabs){
        _selectedTab.value = newTab
    }
}