package com.example.mypin.data.repository

import com.example.mypin.domain.model.Pin
import com.example.mypin.domain.repository.PinRepository
import java.util.UUID

class PinRepositoryImpl : PinRepository {

    override suspend fun savePin(pin: Pin): Result<Pin> {
        return try {
            val savedPin = pin.copy(id = UUID.randomUUID().toString())
            Result.success(savedPin)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
