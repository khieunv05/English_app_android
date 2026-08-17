package com.example.englishapplication.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapplication.domain.model.CreateUserRequest
import com.example.englishapplication.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState

    private val _usernameTextField = MutableStateFlow("")
    val usernameTextField: StateFlow<String> = _usernameTextField

    private val _passwordTextField = MutableStateFlow("")
    val passwordTextField: StateFlow<String> = _passwordTextField

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible : StateFlow<Boolean> = _passwordVisible

    private val _successMessage = MutableStateFlow("")
    val successMessage : StateFlow<String> = _successMessage
    fun onUsernameChanged(username: String) {
        _usernameTextField.value = username
    }

    fun onPasswordChanged(password: String) {
        _passwordTextField.value = password
    }
    fun onPasswordVisibleChange(){
        _passwordVisible.value = !passwordVisible.value
    }
    fun changeSuccessMessage(message: String){
        _successMessage.value = message
    }
    fun signUp() {
        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading
            try {
                val createUserRequest = CreateUserRequest(_usernameTextField.value,
                    _passwordTextField.value)
                val response = userRepository.createUser(createUserRequest)
                if (response.isSuccessful) {
                    _uiState.value = SignUpUiState.Success("Tạo tài khoản thành công")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Gặp lỗi khi tạo tài khoản"
                    _uiState.value = SignUpUiState.Error(errorMsg)
                }
            }
            catch (e: Exception){
                _uiState.value = SignUpUiState.Error(e.message ?: "Gặp lỗi khi tạo tài khoản")
            }
        }
    }
}