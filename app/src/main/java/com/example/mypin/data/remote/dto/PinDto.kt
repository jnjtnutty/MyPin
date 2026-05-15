package com.example.mypin.data.remote.dto

import com.example.mypin.domain.model.PinEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PinDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("category") val category: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("thumbnail_url") val thumbnailUrl: String = "",
    @SerialName("rating") val rating: Double = 0.0,
    @SerialName("distance_km") val distanceKm: Double = 0.0
)

fun PinDto.toDomain(): PinEntity = PinEntity(
    id = id,
    name = name,
    category = category,
    latitude = latitude,
    longitude = longitude,
    thumbnailUrl = thumbnailUrl,
    rating = rating,
    distanceKm = distanceKm
)
