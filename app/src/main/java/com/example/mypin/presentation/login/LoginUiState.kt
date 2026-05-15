package com.example.mypin.presentation.login

sealed class LoginUiState<out T> {
    object Idle : LoginUiState<Nothing>()
    object Loading : LoginUiState<Nothing>()
    data class Success<T>(val data: T) : LoginUiState<T>()
    data class Error(val message: String) : LoginUiState<Nothing>()
}
