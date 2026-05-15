package com.example.mypin.domain.repository

import com.example.mypin.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
}
