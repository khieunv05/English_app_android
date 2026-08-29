package com.example.englishapplication.presentation.paragraph_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapplication.domain.model.CreatePhraseRequest
import com.example.englishapplication.domain.model.GeminiPhraseRequest
import com.example.englishapplication.domain.model.GeminiPhraseResponse
import com.example.englishapplication.domain.model.GrammarErrorsRequest
import com.example.englishapplication.domain.model.PhraseResponse
import com.example.englishapplication.domain.repository.GeminiRepository
import com.example.englishapplication.domain.repository.PhraseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddParagraphViewModel @Inject constructor(
    private val phraseRepository: PhraseRepository,
    private val geminiRepository: GeminiRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow<AddParagraphUiState>(AddParagraphUiState.Idle)

    val uiState: StateFlow<AddParagraphUiState> = _uiState

    private val _text = MutableStateFlow("")

    val text : StateFlow<String> = _text

    private val _geminiPhraseResponse = MutableStateFlow<GeminiPhraseResponse?>(null)

    private val _phraseResponse = MutableStateFlow<PhraseResponse?>(null)

    val phraseResponse: StateFlow<PhraseResponse?> = _phraseResponse

    fun onTextChange(value: String){
        _text.value = value
    }

    fun saveParagraph() {
        viewModelScope.launch {
            _uiState.value = AddParagraphUiState.Loading
            scoreParagraph()
            if(_geminiPhraseResponse.value == null){
                _uiState.value = AddParagraphUiState.Error("Chấm điểm thất bại, vui lòng thử lại sau")
            }
            else{
                val grammarErrorRequest = _geminiPhraseResponse.value?.grammarErrors?.map { error ->
                    GrammarErrorsRequest(
                        incorrect = error.incorrect,
                        correction = error.correction,
                        explanation = error.explanation
                    )
                }
                val createPhraseRequest = CreatePhraseRequest(
                    text = _geminiPhraseResponse.value?.text,
                    score = _geminiPhraseResponse.value?.score?:0,
                    grammarErrors = grammarErrorRequest,
                    correctedText = _geminiPhraseResponse.value?.correctedText
                )
                phraseRepository.createPhrase(createPhraseRequest)
                    .onSuccess {data->
                        _phraseResponse.value = data
                        _uiState.value = AddParagraphUiState.Success("Chấm điểm thành công")
                    }.onFailure { error ->
                        _uiState.value = AddParagraphUiState.Error(error.message ?: "Lỗi hệ thống")
                    }
            }

        }
    }
    fun scoreParagraph() {
        viewModelScope.launch {
            val geminiPhraseRequest = GeminiPhraseRequest(_text.value)
            val response = geminiRepository.scoreParagraph(geminiPhraseRequest)
                .onSuccess { data->
                    _geminiPhraseResponse.value = GeminiPhraseResponse(data.text,
                        data.score,data.grammarErrors,data.correctedText)
                }
                .onFailure { error->
                    _geminiPhraseResponse.value = null
                }
        }
    }

}