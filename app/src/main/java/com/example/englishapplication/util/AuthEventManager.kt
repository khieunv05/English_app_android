package com.example.englishapplication.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthEventManager @Inject constructor() {

    private val _authEvents = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
    val authEvents: SharedFlow<AuthEvent> = _authEvents

    suspend fun notifyUnauthorized() {
        _authEvents.emit(AuthEvent.Unauthorized)
    }
}

sealed class AuthEvent {
    object Unauthorized : AuthEvent()
}