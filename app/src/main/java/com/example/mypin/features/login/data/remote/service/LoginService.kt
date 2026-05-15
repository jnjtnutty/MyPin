package com.example.mypin.features.login.data.remote.service

import com.example.mypin.features.login.data.remote.dto.LoginRequestDto
import com.example.mypin.features.login.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
}
