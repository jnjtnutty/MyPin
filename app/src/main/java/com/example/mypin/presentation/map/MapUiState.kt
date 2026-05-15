package com.example.mypin.presentation.map

import com.example.mypin.domain.model.PinEntity

sealed class MapUiState {
    object Loading : MapUiState()
    data class Success(
        val pins: List<PinEntity>,
        val filteredPins: List<PinEntity> = pins,
        val selectedPinId: String? = null,
        val selectedCategory: String? = null
    ) : MapUiState()
    data class Error(val message: String) : MapUiState()
}
