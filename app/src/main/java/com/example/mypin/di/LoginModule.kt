package com.example.mypin.di

import com.example.mypin.data.local.LoginLocalDataSource
import com.example.mypin.data.repository.LoginRepositoryImpl
import com.example.mypin.domain.repository.LoginRepository
import com.example.mypin.domain.usecase.LoginUseCase
import com.example.mypin.presentation.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single { LoginLocalDataSource(get()) }
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}
