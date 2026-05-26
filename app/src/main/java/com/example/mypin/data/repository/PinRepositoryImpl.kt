package com.example.mypin.data.repository

import com.example.mypin.domain.model.PinItem
import com.example.mypin.domain.repository.PinRepository

class PinRepositoryImpl : PinRepository {

    private val mockPins = listOf(
        PinItem(
            id = "1",
            name = "Blue Bottle Coffee",
            category = "Coffee",
            latitude = 37.7767,
            longitude = -122.3947,
            thumbnailUrl = "https://picsum.photos/seed/bluebottle/100/100",
            rating = 4.8,
            distance = 1.2
        ),
        PinItem(
            id = "2",
            name = "Tartine Bakery",
            category = "Food",
            latitude = 37.7614,
            longitude = -122.4241,
            thumbnailUrl = "https://picsum.photos/seed/tartine/100/100",
            rating = 4.5,
            distance = 2.1
        ),
        PinItem(
            id = "3",
            name = "SFMOMA",
            category = "Art",
            latitude = 37.7857,
            longitude = -122.4011,
            thumbnailUrl = "https://picsum.photos/seed/sfmoma/100/100",
            rating = 4.7,
            distance = 0.4
        ),
        PinItem(
            id = "4",
            name = "Ferry Building",
            category = "Shopping",
            latitude = 37.7955,
            longitude = -122.3937,
            thumbnailUrl = "https://picsum.photos/seed/ferry/100/100",
            rating = 4.6,
            distance = 0.7
        ),
        PinItem(
            id = "5",
            name = "Smitten Ice Cream",
            category = "Food",
            latitude = 37.7641,
            longitude = -122.4200,
            thumbnailUrl = "https://picsum.photos/seed/smitten/100/100",
            rating = 4.4,
            distance = 1.8
        ),
        PinItem(
            id = "6",
            name = "The Battery",
            category = "Nightlife",
            latitude = 37.7944,
            longitude = -122.3958,
            thumbnailUrl = "https://picsum.photos/seed/battery/100/100",
            rating = 4.3,
            distance = 1.0
        ),
        PinItem(
            id = "7",
            name = "Golden Gate Park",
            category = "Nature",
            latitude = 37.7694,
            longitude = -122.4862,
            thumbnailUrl = "https://picsum.photos/seed/goldengate/100/100",
            rating = 4.9,
            distance = 3.5
        ),
        PinItem(
            id = "8",
            name = "Twin Peaks",
            category = "Nature",
            latitude = 37.7544,
            longitude = -122.4477,
            thumbnailUrl = "https://picsum.photos/seed/twinpeaks/100/100",
            rating = 4.6,
            distance = 4.2
        )
    )

    override suspend fun getPins(): Result<List<PinItem>> {
        return Result.success(mockPins)
    }
}
