package com.example.mypin.data.remote.dto

import com.example.mypin.domain.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponseDto(
    @Json(name = "id") val id: String,
    @Json(name = "email") val email: String,
    @Json(name = "name") val name: String
) {
    fun toDomain(): User = User(
        id = id,
        email = email,
        name = name
    )
}
