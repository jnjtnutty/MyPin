package com.example.mypin.di

import com.example.mypin.data.repository.PinRepositoryImpl
import com.example.mypin.domain.repository.PinRepository
import com.example.mypin.domain.usecase.GetPinsUseCase
import com.example.mypin.presentation.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {
    single<PinRepository> { PinRepositoryImpl() }
    factory { GetPinsUseCase(get()) }
    viewModel { MapViewModel(get()) }
}
