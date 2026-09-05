package com.example.englishapplication.presentation.word_main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapplication.domain.model.ListWordUpdateRequest
import com.example.englishapplication.domain.model.WordData
import com.example.englishapplication.domain.model.WordResponseWithDate
import com.example.englishapplication.domain.repository.WordRepository
import com.example.englishapplication.util.NavigationEvent
import com.example.englishapplication.util.NavigationEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class WordMainScreenViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val navigationEventManager: NavigationEventManager

): ViewModel(){
    private val _uiState = MutableStateFlow<WordMainScreenUiState>(WordMainScreenUiState.Idle)

    val uiState: StateFlow<WordMainScreenUiState> = _uiState

    private val _userWords = MutableStateFlow<List<WordResponseWithDate>>(emptyList())

    val userWords : StateFlow<List<WordResponseWithDate>> = _userWords

    private val _listWordReview = MutableStateFlow<Set<Long>>(emptySet())

    val listWordReview : StateFlow<Set<Long>> = _listWordReview

    private val _selectedTab = MutableStateFlow(0)

    val selectedTab : StateFlow<Int> = _selectedTab

    fun onChangeSelectedTab(newTab : Int){
        _selectedTab.value = newTab
        _listWordReview.value = emptySet()
    }

    init {
        loadData()
    }

    val filteredWords: StateFlow<List<WordResponseWithDate>> = combine(
        _userWords, _selectedTab
    ) { words, tab ->
        when (tab) {
            1 -> {
                val today = LocalDate.now()
                words
                    .map { entry ->
                        entry.copy(
                            words = entry.words.filter {
                                it.nextReview == null || it.nextReview.toLocalDate() <= today
                            }
                        )
                    }
                    .filter { it.words.isNotEmpty() }
            }
            else -> words
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
    fun deleteWord(wordId: Long){
        _uiState.value = WordMainScreenUiState.Loading

        viewModelScope.launch {
            val response = wordRepository.deleteWord(wordId)
                .onSuccess {
                    _uiState.value = WordMainScreenUiState.DeleteSuccess("Xóa dữ liệu thành công")
                    loadData()
                }
                .onFailure { error ->
                    _uiState.value = WordMainScreenUiState.Error(error.message ?: "Xóa dữ liệu thất bại")
                }
        }
    }
    fun updateListWordReview(){
        _uiState.value = WordMainScreenUiState.Loading
        viewModelScope.launch {
            val wordIds = _listWordReview.value.toList()
            val listWordUpdateReview = ListWordUpdateRequest(wordIds)
            val response = wordRepository.updateListWordUpdate(listWordUpdateReview)
                .onSuccess {
                    _listWordReview.value = emptySet()
                    _uiState.value = WordMainScreenUiState.UpdateSuccess("Cập nhật thành công")
                    loadData()
                }
                .onFailure { error ->
                    _uiState.value = WordMainScreenUiState.Error(error.message ?: "Cập nhật dữ liệu thất bại")
                }
        }
    }
    fun toggleWordReview(id: Long){
        _listWordReview.value = _listWordReview.value.let {
            if (id in it) it - id else it + id
        }
    }
}