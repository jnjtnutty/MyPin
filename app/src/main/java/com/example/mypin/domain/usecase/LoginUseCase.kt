package com.example.mypin.domain.usecase

import com.example.mypin.domain.model.LoginEntity
import com.example.mypin.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend fun execute(email: String, password: String): Result<LoginEntity> {
        return repository.login(email, password)
    }

    suspend fun isLoggedIn(): Boolean = repository.isLoggedIn()

    fun getDemoCredentials(): LoginEntity = repository.getDemoCredentials()
}
