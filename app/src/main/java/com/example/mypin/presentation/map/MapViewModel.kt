package com.example.mypin.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypin.domain.model.PinItem
import com.example.mypin.domain.usecase.GetPinsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val getPinsUseCase: GetPinsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var allPins: List<PinItem> = emptyList()

    init {
        loadPins()
    }

    fun loadPins() {
        viewModelScope.launch {
            _uiState.value = MapUiState.Loading
            getPinsUseCase.execute()
                .onSuccess { pins ->
                    allPins = pins
                    _uiState.value = MapUiState.Success(
                        pins = pins,
                        filteredPins = pins,
                        selectedCategory = "All"
                    )
                }
                .onFailure {
                    _uiState.value = MapUiState.Error(it.message ?: "Failed to load pins")
                }
        }
    }

    fun selectCategory(category: String) {
        val currentState = _uiState.value
        if (currentState !is MapUiState.Success) return

        val filtered = if (category == "All") allPins
        else allPins.filter { it.category.equals(category, ignoreCase = true) }

        _uiState.value = currentState.copy(
            selectedCategory = category,
            filteredPins = filtered,
            selectedPinId = null
        )
    }

    fun selectPin(pinId: String) {
        val currentState = _uiState.value
        if (currentState !is MapUiState.Success) return
        _uiState.value = currentState.copy(selectedPinId = pinId)
    }

    fun clearPinSelection() {
        val currentState = _uiState.value
        if (currentState !is MapUiState.Success) return
        _uiState.value = currentState.copy(selectedPinId = null)
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        val currentState = _uiState.value
        if (currentState !is MapUiState.Success) return
        _uiState.value = currentState.copy(isLocationPermissionGranted = granted)
    }
}
