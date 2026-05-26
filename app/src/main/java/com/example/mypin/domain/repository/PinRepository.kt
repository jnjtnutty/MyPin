package com.example.mypin.domain.repository

import com.example.mypin.domain.model.PinItem

interface PinRepository {
    suspend fun getPins(): Result<List<PinItem>>
}
