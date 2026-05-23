package com.example.mypin.domain.model

data class Pin(
    val id: String = "",
    val placeName: String,
    val category: String? = null,
    val rating: Int = 0,
    val notes: String = "",
    val photoUris: List<String> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: Long = System.currentTimeMillis()
)
