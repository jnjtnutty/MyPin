package com.example.mypin.domain.usecase

import com.example.mypin.domain.model.User
import com.example.mypin.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun execute(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}
