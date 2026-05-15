package com.example.mypin.features.login.domain.usecase

import com.example.mypin.features.login.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend fun execute(email: String, password: String): Result<Unit> {
        return repository.login(email, password).map { }
    }
}
