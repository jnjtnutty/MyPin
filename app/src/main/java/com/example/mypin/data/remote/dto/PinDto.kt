package com.example.mypin.data.remote.dto

import com.example.mypin.domain.model.Pin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PinDto(
    @SerialName("id") val id: String = "",
    @SerialName("place_name") val placeName: String,
    @SerialName("category") val category: String? = null,
    @SerialName("rating") val rating: Int = 0,
    @SerialName("notes") val notes: String = "",
    @SerialName("photo_uris") val photoUris: List<String> = emptyList(),
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("created_at") val createdAt: Long = System.currentTimeMillis()
)

fun PinDto.toDomain(): Pin = Pin(
    id = id,
    placeName = placeName,
    category = category,
    rating = rating,
    notes = notes,
    photoUris = photoUris,
    latitude = latitude,
    longitude = longitude,
    createdAt = createdAt
)
