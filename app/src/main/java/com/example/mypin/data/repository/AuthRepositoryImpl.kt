package com.example.mypin.data.repository

import com.example.mypin.domain.model.User
import com.example.mypin.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {

    companion object {
        private const val DEMO_EMAIL = "nutty@gmail.com"
        private const val DEMO_PASSWORD = "123456"
        private const val DEMO_DISPLAY_NAME = "Nutty"
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                Result.failure(IllegalArgumentException("Email and password must not be empty"))
            } else if (email == DEMO_EMAIL && password == DEMO_PASSWORD) {
                Result.success(User(email = email, displayName = DEMO_DISPLAY_NAME))
            } else {
                Result.failure(IllegalArgumentException("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
