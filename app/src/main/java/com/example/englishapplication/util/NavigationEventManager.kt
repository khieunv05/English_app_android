package com.example.englishapplication.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationEventManager @Inject constructor(
){
    private val _events = MutableStateFlow<NavigationEvent?>(null)
    val events : StateFlow<NavigationEvent?> = _events

     fun requestNavigateToReviewTab(){
        _events.value = NavigationEvent.NavigationToReviewTab
    }
    fun consumeEvent(){
        _events.value = null
    }

}
sealed class NavigationEvent{
    object NavigationToReviewTab : NavigationEvent()
}