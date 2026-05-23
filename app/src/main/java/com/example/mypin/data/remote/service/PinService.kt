package com.example.mypin.data.remote.service

import com.example.mypin.data.remote.dto.PinDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PinService {
    @POST("pins")
    suspend fun savePin(@Body pin: PinDto): PinDto
}
