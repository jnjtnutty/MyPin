package com.example.mypin.domain.usecase

import com.example.mypin.domain.model.PinItem
import com.example.mypin.domain.repository.PinRepository

class GetPinsUseCase(
    private val pinRepository: PinRepository
) {
    suspend fun execute(): Result<List<PinItem>> {
        return pinRepository.getPins()
    }
}
