package com.example.mypin.features.login.data.repository

import com.example.mypin.features.login.data.remote.dto.LoginRequestDto
import com.example.mypin.features.login.data.remote.service.LoginService
import com.example.mypin.features.login.domain.model.LoginEntity
import com.example.mypin.features.login.domain.repository.LoginRepository

class LoginRepositoryImpl(private val service: LoginService) : LoginRepository {
    override suspend fun login(email: String, password: String): Result<LoginEntity> {
        return try {
            val response = service.login(LoginRequestDto(email, password))
            Result.success(LoginEntity(email = email, password = password))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
