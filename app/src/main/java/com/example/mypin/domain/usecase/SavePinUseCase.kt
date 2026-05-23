package com.example.mypin.domain.usecase

import com.example.mypin.domain.model.Pin
import com.example.mypin.domain.repository.PinRepository

class SavePinUseCase(
    private val pinRepository: PinRepository
) {
    suspend fun execute(pin: Pin): Result<Pin> {
        if (pin.placeName.isBlank()) {
            return Result.failure(IllegalArgumentException("Place name is required"))
        }
        return pinRepository.savePin(pin)
    }
}
