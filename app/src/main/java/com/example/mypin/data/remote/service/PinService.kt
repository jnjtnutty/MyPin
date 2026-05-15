package com.example.mypin.data.remote.service

import com.example.mypin.data.remote.dto.PinDto
import retrofit2.http.GET

interface PinService {
    @GET("pins")
    suspend fun getPins(): List<PinDto>
}
