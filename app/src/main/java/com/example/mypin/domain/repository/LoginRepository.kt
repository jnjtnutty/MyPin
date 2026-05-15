package com.example.mypin.domain.repository

import com.example.mypin.domain.model.LoginEntity

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<LoginEntity>
    suspend fun isLoggedIn(): Boolean
    suspend fun saveLoginState(loggedIn: Boolean)
    fun getDemoCredentials(): LoginEntity
}
