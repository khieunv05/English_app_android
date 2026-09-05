package com.example.englishapplication.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationEventManager @Inject constructor(
){
    private val _events = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val events : SharedFlow<NavigationEvent> = _events

    suspend fun requestNavigateToReviewTab(){
        _events.emit(NavigationEvent.NavigationToReviewTab)
    }

}
sealed class NavigationEvent{
    object NavigationToReviewTab : NavigationEvent()
}