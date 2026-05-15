package com.example.mypin.data.remote.service

import com.example.mypin.data.remote.dto.LoginRequestDto
import com.example.mypin.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
}
