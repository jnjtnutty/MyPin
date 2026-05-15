package com.example.mypin.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private companion object {
        const val DEMO_EMAIL = "nutty@gmail.com"
        const val DEMO_PASSWORD = "123456"
        const val SIMULATED_DELAY_MS = 800L
    }

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChanged(value: String) {
        _email.value = value
        if (_uiState.value is LoginUiState.Error) {
            _uiState.value = LoginUiState.Idle
        }
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
        if (_uiState.value is LoginUiState.Error) {
            _uiState.value = LoginUiState.Idle
        }
    }

    fun fillDemoAccount() {
        _email.value = DEMO_EMAIL
        _password.value = DEMO_PASSWORD
        _uiState.value = LoginUiState.Idle
    }

    fun signIn() {
        val emailValue = _email.value.trim()
        val passwordValue = _password.value

        if (emailValue.isBlank()) {
            _uiState.value = LoginUiState.Error("Please enter your email")
            return
        }
        if (passwordValue.isBlank()) {
            _uiState.value = LoginUiState.Error("Please enter your password")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            delay(SIMULATED_DELAY_MS)

            if (emailValue == DEMO_EMAIL && passwordValue == DEMO_PASSWORD) {
                _uiState.value = LoginUiState.Success(emailValue)
            } else {
                _uiState.value = LoginUiState.Error("Invalid email or password")
            }
        }
    }

    fun clearError() {
        _uiState.value = LoginUiState.Idle
    }
}
