package com.example.mypin.features.login.domain.repository

import com.example.mypin.features.login.domain.model.LoginEntity

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<LoginEntity>
}
