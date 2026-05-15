package com.example.mypin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypin.domain.model.LoginEntity
import com.example.mypin.domain.usecase.LoginUseCase
import com.example.mypin.presentation.ui.UiState
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<LoginEntity>>()
    val loginState: LiveData<UiState<LoginEntity>> = _loginState

    init {
        _loginState.value = UiState.Idle
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            loginUseCase.execute(email, password)
                .onSuccess { _loginState.value = UiState.Success(it) }
                .onFailure { _loginState.value = UiState.Error(it.message ?: "Unknown error") }
        }
    }

    fun fillDemoCredentials(): LoginEntity {
        return loginUseCase.getDemoCredentials()
    }

    fun resetState() {
        _loginState.value = UiState.Idle
    }
}
