package com.example.englishapplication.presentation.add_word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.domain.model.CreateWordRequest
import com.example.englishapplication.domain.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWordViewModel @Inject constructor(private val wordRepository: WordRepository,
    private val encryptedTokenStorage: EncryptedTokenStorage ): ViewModel() {
    private val _uiState = MutableStateFlow<AddWordUiState>(AddWordUiState.Idle)
    val uiState : StateFlow<AddWordUiState> = _uiState

    private val _englishTextField = MutableStateFlow("")
    val englishTextField : StateFlow<String> = _englishTextField

    private val _vietnameseTextField = MutableStateFlow("")
    val vietnameseTextField : StateFlow<String> = _vietnameseTextField

    private val _exampleTextField = MutableStateFlow("")
    val exampleTextField : StateFlow<String> = _exampleTextField

    private val _exampleTranslationTextField = MutableStateFlow("")
    val exampleTranslationTextField : StateFlow<String> = _exampleTranslationTextField

    private val _levelTextField = MutableStateFlow("")
    val levelTextField : StateFlow<String> = _levelTextField

    private val _partOfSpeechTextField = MutableStateFlow("")
    val partOfSpeechTextField : StateFlow<String> = _partOfSpeechTextField

    private val _pronunciationTextField = MutableStateFlow("")
    val pronunciationTextField : StateFlow<String> = _pronunciationTextField

    fun onEnglishTextFieldChange(newValue: String){
        _englishTextField.value = newValue
    }

    fun changeVietnameseText(newValue: String){
        _vietnameseTextField.value = newValue
    }

    fun changeExampleText(newValue: String){
        _exampleTextField.value = newValue
    }

    fun changeExampleTranslationText(newValue: String){
        _exampleTranslationTextField.value = newValue
    }

    fun changeLevelText(newValue: String){
        _levelTextField.value = newValue
    }

    fun changePartOfSpeechText(newValue: String){
        _partOfSpeechTextField.value = newValue
    }

    fun changePronunciationText(newValue: String){
        _pronunciationTextField.value = newValue
    }

    fun addWord(){
        _uiState.value = AddWordUiState.Loading
        viewModelScope.launch {
            try {
                val createWordRequest = CreateWordRequest(
                    english = _englishTextField.value,
                    vietnamese = _vietnameseTextField.value,
                    example = _exampleTextField.value,
                    exampleTranslation = _exampleTranslationTextField.value,
                    level = _levelTextField.value,
                    partOfSpeech = _partOfSpeechTextField.value,
                    pronunciation = _pronunciationTextField.value
                )
                wordRepository.createWord(createWordRequest)
                _uiState.value = AddWordUiState.Success("Thêm từ mới thành công")
                _englishTextField.value = ""
                _vietnameseTextField.value = ""
                _exampleTextField.value = ""
                _exampleTranslationTextField.value = ""
                _levelTextField.value = ""
                _partOfSpeechTextField.value = ""
                _pronunciationTextField.value = ""
            } catch (e: Exception) {
                _uiState.value = AddWordUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

}