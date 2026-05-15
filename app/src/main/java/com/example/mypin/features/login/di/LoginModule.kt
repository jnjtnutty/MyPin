package com.example.mypin.features.login.di

import com.example.mypin.features.login.data.remote.service.LoginService
import com.example.mypin.features.login.data.repository.LoginRepositoryImpl
import com.example.mypin.features.login.domain.repository.LoginRepository
import com.example.mypin.features.login.domain.usecase.LoginUseCase
import com.example.mypin.features.login.presentation.viewmodel.LoginViewModel
import retrofit2.Retrofit

object LoginModule {

    private lateinit var retrofit: Retrofit

    fun init(retrofit: Retrofit) {
        this.retrofit = retrofit
    }

    private val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    private val loginRepository: LoginRepository by lazy {
        LoginRepositoryImpl(loginService)
    }

    private val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(loginRepository)
    }

    val loginViewModelFactory: androidx.lifecycle.ViewModelProvider.Factory
        get() = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(loginUseCase) as T
            }
        }
}
