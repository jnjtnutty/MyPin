// Figma: https://www.figma.com/design/RbxZunWIJGyF1YrWcgE54q/MyPin-Mobile-Login-Design?node-id=10-2
package com.example.mypin.presentation.addpin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.mypin.ui.theme.MyPinTheme

private val White = Color(0xFFFFFFFF)
private val Background = Color(0xFFFFFFFF)
private val CardBackground = Color(0xFFF5F6F7)
private val BorderColor = Color(0xFFE6E8EB)
private val TextPrimary = Color(0xFF212529)
private val TextSecondary = Color(0xFF6B7280)
private val MapGradientStart = Color(0xFFD4E3F2)
private val MapGradientEnd = Color(0xFFA8C2DB)
private val DashedBorderColor = Color(0xFFBFC9D9)
private val ErrorRed = Color(0xFFE53E3E)

private val CATEGORIES = listOf("Food", "Coffee", "Nature", "Art", "Nightlife", "Shopping")

@Composable
fun AddPinScreen(
    viewModel: AddPinViewModel,
    onClose: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var showPlaceNameError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(saveState) {
        if (saveState is SavePinUiState.Success) {
            viewModel.resetState()
            onSaved()
        }
    }

    val onSaveClick = {
        if (!formState.isValid) {
            showPlaceNameError = true
        } else {
            viewModel.savePin()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        val remaining = AddPinViewModel.MAX_PHOTOS - formState.photoUris.size
        uris.take(remaining).forEach { viewModel.addPhoto(it.toString()) }
    }

    Column(modifier = modifier.fillMaxSize().background(Background)) {
        AddPinTopNav(
            onClose = onClose,
            onSave = onSaveClick
        )

        Box(Modifier.weight(1f).imePadding()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 108.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                FieldLabel("LOCATION")
                Spacer(Modifier.height(8.dp))
                LocationCard(onTap = { })
                Spacer(Modifier.height(20.dp))
                FieldLabel("PLACE NAME")
                Spacer(Modifier.height(8.dp))
                PlaceNameInput(
                    value = formState.placeName,
                    onValueChange = {
                        viewModel.onPlaceNameChange(it)
                        if (it.isNotBlank()) showPlaceNameError = false
                    },
                    isError = showPlaceNameError && formState.placeName.isBlank()
                )
                if (showPlaceNameError && formState.placeName.isBlank()) {
                    Text(
                        text = "Place name is required",
                        color = ErrorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                FieldLabel("CATEGORY")
                Spacer(Modifier.height(8.dp))
                CategoryChips(
                    selected = formState.category,
                    onSelected = { viewModel.onCategoryChange(it) }
                )
                Spacer(Modifier.height(20.dp))
                FieldLabel("YOUR RATING")
                Spacer(Modifier.height(8.dp))
                RatingStars(
                    rating = formState.rating,
                    onRatingChange = { viewModel.onRatingChange(it) }
                )
                Spacer(Modifier.height(24.dp))
                FieldLabel("NOTES")
                Spacer(Modifier.height(8.dp))
                NotesTextarea(
                    value = formState.notes,
                    onValueChange = { viewModel.onNotesChange(it) }
                )
                Spacer(Modifier.height(16.dp))
                FieldLabel("PHOTOS")
                Spacer(Modifier.height(8.dp))
                PhotoGallery(
                    photoUris = formState.photoUris,
                    onAddPhoto = {
                        val remaining = AddPinViewModel.MAX_PHOTOS - formState.photoUris.size
                        if (remaining > 0) {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    },
                    onRemovePhoto = { viewModel.removePhoto(it) }
                )
                Spacer(Modifier.height(16.dp))
            }

            BottomSaveBar(
                enabled = formState.isValid && saveState !is SavePinUiState.Saving,
                isSaving = saveState is SavePinUiState.Saving,
                onSave = onSaveClick,
                errorMessage = (saveState as? SavePinUiState.Error)?.message,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun AddPinTopNav(
    onClose: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(White)
            .padding(top = 6.dp, bottom = 12.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(40.dp)
                .background(CardBackground, RoundedCornerShape(999.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = "Add Pin",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = onSave) {
            Text(
                text = "Save",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
    HorizontalDivider(color = BorderColor, thickness = 1.dp)
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextSecondary
    )
}

@Composable
private fun LocationCard(onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .background(CardBackground, RoundedCornerShape(16.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onTap)
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(MapGradientStart, MapGradientEnd)
                    ),
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(134.dp)
                    .height(2.dp)
                    .graphicsLayer { rotationZ = -20f }
                    .background(White.copy(alpha = 0.7f))
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = "Current location",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tap to drop a pin on map",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun PlaceNameInput(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Enter place name",
                color = TextSecondary,
                fontSize = 15.sp
            )
        },
        textStyle = TextStyle(fontSize = 15.sp, color = TextPrimary),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground,
            cursorColor = TextPrimary,
            focusedBorderColor = if (isError) ErrorRed else BorderColor,
            unfocusedBorderColor = if (isError) ErrorRed else BorderColor
        ),
        singleLine = true
    )
}

@Composable
private fun CategoryChips(
    selected: String?,
    onSelected: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CATEGORIES.forEach { category ->
            val isSelected = category == selected
            val bgColor = if (isSelected) TextPrimary else CardBackground
            val textColor = if (isSelected) White else TextPrimary
            val borderModifier = if (isSelected) Modifier
            else Modifier.border(1.dp, BorderColor, RoundedCornerShape(999.dp))

            Row(
                modifier = Modifier
                    .then(borderModifier)
                    .background(bgColor, RoundedCornerShape(999.dp))
                    .clickable { onSelected(if (isSelected) null else category) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}

@Composable
private fun RatingStars(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in 1..5) {
            val isFilled = i <= rating
            IconButton(
                onClick = { onRatingChange(if (i == rating) 0 else i) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Star $i",
                    tint = if (isFilled) TextPrimary else TextSecondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun NotesTextarea(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        placeholder = {
            Text(
                text = "What made this place special? (vibe, must-order, hours…)",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        },
        textStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, color = TextPrimary),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground,
            cursorColor = TextPrimary,
            focusedBorderColor = BorderColor,
            unfocusedBorderColor = BorderColor
        )
    )
}

@Composable
private fun PhotoGallery(
    photoUris: List<String>,
    onAddPhoto: () -> Unit,
    onRemovePhoto: (String) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        photoUris.forEach { uri ->
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(CardBackground, RoundedCornerShape(12.dp))
                    .clickable { onRemovePhoto(uri) }
            ) {
                AsyncImage(
                    model = Uri.parse(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(18.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(999.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
        if (photoUris.size < AddPinViewModel.MAX_PHOTOS) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = DashedBorderColor,
                            style = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                        )
                    }
                    .background(CardBackground.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .clickable(onClick = onAddPhoto),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add photo",
                    tint = DashedBorderColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomSaveBar(
    enabled: Boolean,
    isSaving: Boolean,
    onSave: () -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
    ) {
        HorizontalDivider(color = BorderColor, thickness = 1.dp)
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = ErrorRed,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp)
            )
        }
        Box(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Button(
                onClick = onSave,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TextPrimary,
                    contentColor = White,
                    disabledContainerColor = TextPrimary.copy(alpha = 0.4f),
                    disabledContentColor = White.copy(alpha = 0.6f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp, 18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isSaving) "Saving…" else "Save Pin",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddPinTopNavPreview() {
    MyPinTheme { AddPinTopNav(onClose = {}, onSave = {}) }
}

@Preview
@Composable
private fun LocationCardPreview() {
    MyPinTheme { LocationCard(onTap = {}) }
}

@Preview
@Composable
private fun PlaceNameInputPreview() {
    MyPinTheme { PlaceNameInput(value = "Blue Bottle Coffee", onValueChange = {}, isError = false) }
}

@Preview
@Composable
private fun PlaceNameInputErrorPreview() {
    MyPinTheme { PlaceNameInput(value = "", onValueChange = {}, isError = true) }
}

@Preview
@Composable
private fun CategoryChipsPreview() {
    MyPinTheme { CategoryChips(selected = "Coffee", onSelected = {}) }
}

@Preview
@Composable
private fun RatingStarsPreview() {
    MyPinTheme { RatingStars(rating = 3, onRatingChange = {}) }
}

@Preview
@Composable
private fun NotesTextareaPreview() {
    MyPinTheme { NotesTextarea(value = "", onValueChange = {}) }
}

@Preview
@Composable
private fun PhotoGalleryPreview() {
    MyPinTheme { PhotoGallery(photoUris = emptyList(), onAddPhoto = {}, onRemovePhoto = {}) }
}

@Preview
@Composable
private fun BottomSaveBarPreview() {
    MyPinTheme { BottomSaveBar(enabled = true, isSaving = false, onSave = {}, errorMessage = null) }
}

@Preview
@Composable
private fun BottomSaveBarDisabledPreview() {
    MyPinTheme { BottomSaveBar(enabled = false, isSaving = false, onSave = {}, errorMessage = null) }
}

@Preview
@Composable
private fun BottomSaveBarErrorPreview() {
    MyPinTheme {
        BottomSaveBar(enabled = true, isSaving = false, onSave = {}, errorMessage = "Network error")
    }
}

@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun AddPinScreenPreview() {
    MyPinTheme {
        AddPinScreen(
            viewModel = AddPinViewModel(
                savePinUseCase = com.example.mypin.domain.usecase.SavePinUseCase(
                    com.example.mypin.data.repository.PinRepositoryImpl()
                )
            ),
            onClose = {},
            onSaved = {}
        )
    }
}
