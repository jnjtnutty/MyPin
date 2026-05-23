package com.example.mypin.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypin.presentation.addpin.AddPinScreen
import com.example.mypin.presentation.addpin.AddPinViewModel
import com.example.mypin.presentation.map.MapScreen
import com.example.mypin.ui.theme.MyPinTheme
import org.koin.androidx.compose.koinViewModel

private val White = Color(0xFFFFFFFF)
private val TabActiveColor = Color(0xFF212529)
private val TabInactiveColor = Color(0xFF6B7280)
private val DividerColor = Color(0xFFE6E8EB)

private data class TabItem(
    val label: String,
    val icon: ImageVector
)

private val TABS = listOf(
    TabItem("Map", Icons.Default.LocationOn),
    TabItem("My Pins", Icons.Default.Bookmark),
    TabItem("Add", Icons.Default.AddCircle),
    TabItem("Profile", Icons.Default.Person)
)

@Composable
fun MainTabScreen(
    modifier: Modifier = Modifier
) {
    val addPinViewModel: AddPinViewModel = koinViewModel()
    var selectedTab by rememberSaveable { mutableStateOf(2) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var pendingNavigation by remember { mutableStateOf<(() -> Unit)?>(null) }

    fun requestNavigation(navigate: () -> Unit) {
        if (selectedTab == 2 && addPinViewModel.hasUnsavedChanges) {
            pendingNavigation = navigate
            showDiscardDialog = true
        } else {
            navigate()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> MapScreen()
                1 -> MyPinsPlaceholder()
                2 -> AddPinScreen(
                    viewModel = addPinViewModel,
                    onClose = { requestNavigation { selectedTab = 0 } },
                    onSaved = { selectedTab = 0 }
                )
                3 -> ProfilePlaceholder()
                else -> MapScreen()
            }
        }
        TabBar(
            selectedTab = selectedTab,
            onTabSelected = { newTab ->
                if (newTab != selectedTab) {
                    requestNavigation { selectedTab = newTab }
                }
            }
        )
    }

    if (showDiscardDialog) {
        DiscardChangesDialog(
            onDiscard = {
                pendingNavigation?.invoke()
                pendingNavigation = null
                showDiscardDialog = false
            },
            onCancel = {
                pendingNavigation = null
                showDiscardDialog = false
            }
        )
    }
}

@Composable
private fun TabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Column {
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 24.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TABS.forEachIndexed { index, tab ->
                val isActive = index == selectedTab
                val color = if (isActive) TabActiveColor else TabInactiveColor
                Column(
                    modifier = Modifier
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = color,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = color
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscardChangesDialog(
    onDiscard: () -> Unit,
    onCancel: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = "Discard changes?", fontWeight = FontWeight.SemiBold)
        },
        text = {
            Text("You have unsaved changes. Are you sure you want to leave?")
        },
        confirmButton = {
            TextButton(onClick = onDiscard) {
                Text("Discard", color = Color(0xFFE53E3E), fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel", fontWeight = FontWeight.SemiBold)
            }
        }
    )
}

@Composable
private fun MyPinsPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(text = "My Pins", fontSize = 24.sp)
        }
    }
}

@Composable
private fun ProfilePlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(text = "Profile", fontSize = 24.sp)
        }
    }
}

@Preview
@Composable
private fun TabBarPreview() {
    MyPinTheme { TabBar(selectedTab = 2, onTabSelected = {}) }
}
