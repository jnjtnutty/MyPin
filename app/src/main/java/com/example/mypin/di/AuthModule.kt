package com.example.mypin.di

import com.example.mypin.data.remote.service.AuthService
import com.example.mypin.data.repository.AuthRepositoryImpl
import com.example.mypin.domain.repository.AuthRepository
import com.example.mypin.domain.usecase.LoginUseCase
import com.example.mypin.presentation.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    single<AuthService> { get<Retrofit>().create(AuthService::class.java) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}
