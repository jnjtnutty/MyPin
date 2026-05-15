package com.example.mypin.di

import com.example.mypin.data.repository.AuthRepositoryImpl
import com.example.mypin.domain.repository.AuthRepository
import com.example.mypin.domain.usecase.LoginUseCase
import com.example.mypin.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}
