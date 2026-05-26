package com.example.mypin.data.repository

import com.example.mypin.data.remote.dto.PinDto
import com.example.mypin.data.remote.dto.toDomain
import com.example.mypin.domain.model.PinEntity
import com.example.mypin.domain.repository.PinRepository

class PinRepositoryImpl : PinRepository {

    private val demoPins = listOf(
        PinDto(
            id = "pin_sfmoma",
            name = "SFMOMA",
            category = "Art",
            latitude = 37.7857,
            longitude = -122.4011,
            thumbnailUrl = "",
            rating = 4.7,
            distanceKm = 0.4
        ),
        PinDto(
            id = "pin_ferry_building",
            name = "Ferry Building",
            category = "Shopping",
            latitude = 37.7955,
            longitude = -122.3937,
            thumbnailUrl = "",
            rating = 4.6,
            distanceKm = 0.7
        ),
        PinDto(
            id = "pin_blue_bottle",
            name = "Blue Bottle Coffee",
            category = "Coffee",
            latitude = 37.7849,
            longitude = -122.4094,
            thumbnailUrl = "",
            rating = 4.8,
            distanceKm = 1.2
        ),
        PinDto(
            id = "pin_tartine",
            name = "Tartine Bakery",
            category = "Food",
            latitude = 37.7913,
            longitude = -122.4094,
            thumbnailUrl = "",
            rating = 4.9,
            distanceKm = 0.5
        ),
        PinDto(
            id = "pin_smitten",
            name = "Smitten Ice Cream",
            category = "Food",
            latitude = 37.7617,
            longitude = -122.4245,
            thumbnailUrl = "",
            rating = 4.5,
            distanceKm = 2.1
        ),
        PinDto(
            id = "pin_the_battery",
            name = "The Battery",
            category = "Nightlife",
            latitude = 37.7880,
            longitude = -122.3990,
            thumbnailUrl = "",
            rating = 4.3,
            distanceKm = 0.9
        ),
        PinDto(
            id = "pin_golden_gate_park",
            name = "Golden Gate Park",
            category = "Nature",
            latitude = 37.7694,
            longitude = -122.4862,
            thumbnailUrl = "",
            rating = 4.9,
            distanceKm = 3.5
        ),
        PinDto(
            id = "pin_twin_peaks",
            name = "Twin Peaks",
            category = "Nature",
            latitude = 37.7544,
            longitude = -122.4477,
            thumbnailUrl = "",
            rating = 4.7,
            distanceKm = 2.8
        )
    )

    override suspend fun getPins(): Result<List<PinEntity>> {
        return try {
            Result.success(demoPins.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
