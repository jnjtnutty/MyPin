package com.example.mypin.domain.usecase

import com.example.mypin.domain.model.PinEntity
import com.example.mypin.domain.repository.PinRepository

class GetPinsUseCase(
    private val pinRepository: PinRepository
) {
    suspend fun execute(): Result<List<PinEntity>> {
        return pinRepository.getPins()
    }
}
