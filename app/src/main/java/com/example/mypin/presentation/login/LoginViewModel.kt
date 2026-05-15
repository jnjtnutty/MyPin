package com.example.mypin.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypin.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState<Unit>>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState<Unit>> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (_uiState.value is LoginUiState.Loading) return
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            loginUseCase.execute(email.trim(), password.trim())
                .onSuccess { _uiState.value = LoginUiState.Success(Unit) }
                .onFailure { _uiState.value = LoginUiState.Error(it.message ?: "Unknown error") }
        }
    }
}
