package com.example.mypin.domain.model

data class PinItem(
    val id: String,
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val thumbnailUrl: String,
    val rating: Double,
    val distance: Double
)
