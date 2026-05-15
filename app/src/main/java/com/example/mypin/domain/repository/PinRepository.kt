package com.example.mypin.domain.repository

import com.example.mypin.domain.model.PinEntity

interface PinRepository {
    suspend fun getPins(): Result<List<PinEntity>>
}
