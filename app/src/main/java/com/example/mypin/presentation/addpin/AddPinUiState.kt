package com.example.mypin.presentation.addpin

data class AddPinFormState(
    val placeName: String = "",
    val category: String? = null,
    val rating: Int = 0,
    val notes: String = "",
    val photoUris: List<String> = emptyList()
) {
    val hasUnsavedChanges: Boolean
        get() = placeName.isNotBlank() || category != null || rating > 0 ||
                notes.isNotBlank() || photoUris.isNotEmpty()

    val isValid: Boolean
        get() = placeName.isNotBlank()
}

sealed class SavePinUiState {
    object Idle : SavePinUiState()
    object Saving : SavePinUiState()
    object Success : SavePinUiState()
    data class Error(val message: String) : SavePinUiState()
}
