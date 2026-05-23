package com.example.mypin.domain.repository

import com.example.mypin.domain.model.Pin

interface PinRepository {
    suspend fun savePin(pin: Pin): Result<Pin>
}
