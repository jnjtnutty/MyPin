package com.example.mypin.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypin.domain.model.PinEntity
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

    init {
        loadPins()
    }

    private fun loadPins() {
        viewModelScope.launch {
            _uiState.value = MapUiState.Loading
            getPinsUseCase.execute()
                .onSuccess { pins ->
                    _uiState.value = MapUiState.Success(
                        pins = pins,
                        filteredPins = pins
                    )
                }
                .onFailure { _uiState.value = MapUiState.Error(it.message ?: "Unknown error") }
        }
    }

    fun selectPin(pinId: String?) {
        val current = _uiState.value
        if (current is MapUiState.Success) {
            _uiState.value = current.copy(selectedPinId = pinId)
        }
    }

    fun filterByCategory(category: String?) {
        val current = _uiState.value
        if (current is MapUiState.Success) {
            val filtered = if (category == null) {
                current.pins
            } else {
                current.pins.filter { it.category == category }
            }
            _uiState.value = current.copy(
                selectedCategory = category,
                filteredPins = filtered,
                selectedPinId = null
            )
        }
    }

    fun retry() = loadPins()
}
