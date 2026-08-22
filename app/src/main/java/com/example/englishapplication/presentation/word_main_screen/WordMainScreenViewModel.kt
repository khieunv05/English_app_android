package com.example.englishapplication.presentation.word_main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapplication.domain.model.WordResponseWithDate
import com.example.englishapplication.domain.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordMainScreenViewModel @Inject constructor(
    private val wordRepository: WordRepository
): ViewModel(){
    private val _uiState = MutableStateFlow<WordMainScreenUiState>(WordMainScreenUiState.Idle)

    val uiState: StateFlow<WordMainScreenUiState> = _uiState

    private val _userWords = MutableStateFlow<List<WordResponseWithDate>>(emptyList())

    val userWords : StateFlow<List<WordResponseWithDate>> = _userWords

    private val _selectedTab = MutableStateFlow(0)

    val selectedTab : StateFlow<Int> = _selectedTab

    fun onChangeSelectedTab(newTab : Int){
        _selectedTab.value = newTab
    }

    init {
        loadData()
    }
    fun loadData(){
        _uiState.value = WordMainScreenUiState.Loading

        viewModelScope.launch {
           val response = wordRepository.getAllWords()
               .onSuccess { value->
                   _userWords.value = value
                   _uiState.value = WordMainScreenUiState.Success(_userWords.value)
               }
               .onFailure { error->
                   _uiState.value = WordMainScreenUiState.Error(error.message ?: "Đã có lỗi xảy ra")
               }



        }
    }
}