package com.example.mypin.presentation.map

import com.example.mypin.domain.model.PinItem

sealed class MapUiState {
    object Loading : MapUiState()
    data class Success(
        val pins: List<PinItem> = emptyList(),
        val filteredPins: List<PinItem> = emptyList(),
        val selectedPinId: String? = null,
        val selectedCategory: String = "All",
        val isLocationPermissionGranted: Boolean = false
    ) : MapUiState()
    data class Error(val message: String) : MapUiState()
}
