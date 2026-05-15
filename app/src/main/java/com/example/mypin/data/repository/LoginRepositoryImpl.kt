package com.example.mypin.data.repository

import com.example.mypin.data.local.LoginLocalDataSource
import com.example.mypin.domain.model.LoginEntity
import com.example.mypin.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val localDataSource: LoginLocalDataSource
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<LoginEntity> {
        return try {
            if (email == LoginLocalDataSource.DEMO_EMAIL && password == LoginLocalDataSource.DEMO_PASSWORD) {
                localDataSource.saveLoginState(true)
                Result.success(LoginEntity(email = email, password = password))
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return localDataSource.isLoggedIn()
    }

    override suspend fun saveLoginState(loggedIn: Boolean) {
        localDataSource.saveLoginState(loggedIn)
    }

    override fun getDemoCredentials(): LoginEntity {
        return LoginEntity(
            email = LoginLocalDataSource.DEMO_EMAIL,
            password = LoginLocalDataSource.DEMO_PASSWORD
        )
    }
}
