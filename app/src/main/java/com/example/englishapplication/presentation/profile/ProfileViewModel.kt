package com.example.englishapplication.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapplication.domain.repository.UserRepository
import com.example.englishapplication.util.AuthEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authEventManager: AuthEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun logout() {
        if (_uiState.value is ProfileUiState.LoggingOut) return

        viewModelScope.launch {
            _uiState.value = ProfileUiState.LoggingOut

            userRepository.logout().onFailure { error ->
                Log.w(TAG, "Không thu hồi được refresh token ở server, vẫn đăng xuất cục bộ", error)
            }

            _uiState.value = ProfileUiState.LoggedOut
            authEventManager.notifyLoggedOut()
        }
    }

    private companion object {
        const val TAG = "ProfileViewModel"
    }
}
