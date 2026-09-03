package com.example.englishapplication.presentation.paragraph_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.englishapplication.domain.model.PhraseResponse
import com.example.englishapplication.domain.repository.GeminiRepository
import com.example.englishapplication.domain.repository.PhraseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParagraphMainViewModel @Inject constructor(
    private val phraseRepository: PhraseRepository,
): ViewModel(){
    private val _uiState = MutableStateFlow<ParagraphMainUiState>(ParagraphMainUiState.Idle)

    val uiState : StateFlow<ParagraphMainUiState> = _uiState

    private val _paragraphs = MutableStateFlow<List<PhraseResponse>>(emptyList())

    val paragraphs : StateFlow<List<PhraseResponse>> = _paragraphs



    init {
        getAllParagraphs()
    }
    fun getAllParagraphs(){
        _uiState.value = ParagraphMainUiState.Loading
        viewModelScope.launch {
            phraseRepository.getAllPhrases()
                .onSuccess { phrases ->
                    _uiState.value = ParagraphMainUiState.Success
                    _paragraphs.value = phrases
                }.onFailure { error ->
                    _uiState.value = ParagraphMainUiState.Error(error.message ?: "An unexpected error occurred")
                }
        }
    }
    fun deleteParagraph(id: Long){
        _uiState.value = ParagraphMainUiState.Loading
        viewModelScope.launch {
            phraseRepository.deletePhrase(id)
                .onSuccess {
                    _uiState.value = ParagraphMainUiState.DeleteSuccess("Xóa thành công")
                    getAllParagraphs()
                }
                .onFailure {error->
                    _uiState.value = ParagraphMainUiState.Error(error.message ?: "Đã có lỗi xảy ra")
                }
        }
    }


}