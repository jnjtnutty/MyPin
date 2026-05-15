package com.example.mypin.data.repository

import com.example.mypin.data.remote.dto.LoginRequestDto
import com.example.mypin.data.remote.service.AuthService
import com.example.mypin.domain.repository.AuthRepository
import com.example.mypin.domain.model.User

class AuthRepositoryImpl(private val service: AuthService) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = service.login(LoginRequestDto(email, password))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
