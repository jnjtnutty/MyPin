package com.example.mypin.presentation.addpin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypin.domain.model.Pin
import com.example.mypin.domain.usecase.SavePinUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddPinViewModel(
    private val savePinUseCase: SavePinUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(AddPinFormState())
    val formState: StateFlow<AddPinFormState> = _formState.asStateFlow()

    private val _saveState = MutableStateFlow<SavePinUiState>(SavePinUiState.Idle)
    val saveState: StateFlow<SavePinUiState> = _saveState.asStateFlow()

    val hasUnsavedChanges: Boolean
        get() = _formState.value.hasUnsavedChanges

    fun onPlaceNameChange(name: String) {
        _formState.update { it.copy(placeName = name) }
        clearErrorIfPresent()
    }

    fun onCategoryChange(category: String?) {
        _formState.update { it.copy(category = category) }
    }

    fun onRatingChange(rating: Int) {
        _formState.update { it.copy(rating = rating.coerceIn(0, 5)) }
    }

    fun onNotesChange(notes: String) {
        _formState.update { it.copy(notes = notes) }
    }

    fun addPhoto(uri: String) {
        _formState.update {
            if (it.photoUris.size >= MAX_PHOTOS) it
            else it.copy(photoUris = it.photoUris + uri)
        }
    }

    fun removePhoto(uri: String) {
        _formState.update { it.copy(photoUris = it.photoUris - uri) }
    }

    fun savePin() {
        val form = _formState.value
        if (!form.isValid) return
        if (_saveState.value is SavePinUiState.Saving) return
        viewModelScope.launch {
            _saveState.value = SavePinUiState.Saving
            savePinUseCase.execute(
                Pin(
                    placeName = form.placeName,
                    category = form.category,
                    rating = form.rating,
                    notes = form.notes,
                    photoUris = form.photoUris
                )
            ).onSuccess { _saveState.value = SavePinUiState.Success }
                .onFailure { _saveState.value = SavePinUiState.Error(it.message ?: "Unknown error") }
        }
    }

    fun resetState() {
        _formState.value = AddPinFormState()
        _saveState.value = SavePinUiState.Idle
    }

    private fun clearErrorIfPresent() {
        if (_saveState.value is SavePinUiState.Error) {
            _saveState.value = SavePinUiState.Idle
        }
    }

    companion object {
        const val MAX_PHOTOS = 10
    }
}
