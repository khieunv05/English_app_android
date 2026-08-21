package com.example.englishapplication.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapplication.data.local.EncryptedTokenStorage
import com.example.englishapplication.domain.model.LoginRequest
import com.example.englishapplication.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository,
    private val encryptedTokenStorage: EncryptedTokenStorage
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState
    private val _usernameTextField = MutableStateFlow("")
    val usernameTextField: StateFlow<String> = _usernameTextField

    private val _passwordTextField = MutableStateFlow("")
    val passwordTextField: StateFlow<String> = _passwordTextField
    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible : StateFlow<Boolean> = _passwordVisible
    fun onUsernameChange(newValue: String) {
        _usernameTextField.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _passwordTextField.value = newValue
    }
    fun onPasswordVisibleChange(){
        _passwordVisible.value = !_passwordVisible.value
    }
    init {
        //checkToken()
    }
    fun login() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val loginRequest = LoginRequest(
                username = _usernameTextField.value,
                password = _passwordTextField.value
            )

            userRepository.login(loginRequest)
                .onSuccess {
                    _uiState.value = LoginUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "An unknown error occurred")
                }
        }
    }
    fun checkToken() {
        viewModelScope.launch {
            val token = encryptedTokenStorage.getAccessToken()
            if(!token.isNullOrEmpty()){
                _uiState.value = LoginUiState.Success
            }
        }

    }

}