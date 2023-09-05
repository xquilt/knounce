package com.polendina.knounce.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Column (
        modifier = Modifier
            .padding(
                horizontal = 20.dp
            )
    ) {
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(50.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null
            )
        }
        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            SettingsItem.values().forEach {
                SettingItemRow(settingsItem = it)
            }
        }
    }
}

fun Modifier.notificationRect(): Modifier =
    composed {
        val tertiary = MaterialTheme.colorScheme.surfaceVariant
        val height = 120F
        val width = 120F
        drawWithContent {
            drawRoundRect(
                color = tertiary,
                size = Size(width, height),
                cornerRadius = CornerRadius(20F, 20F),
                topLeft = Offset(-width/4, -height/4)
            )
            drawContent()
        }
    }

fun Modifier.notificationCircle(): Modifier =
    composed {
        val tertiary = MaterialTheme.colorScheme.primaryContainer
        drawWithContent {
            drawCircle(
                color = tertiary,
                radius = 25.dp.toPx(),
            )
            drawContent()
        }
    }

enum class SettingsItem(
    val category: String,
    val icon: ImageVector,
) {
    // TODO: Icons need to be refined
    ACCOUNT(category = "Account", icon = Icons.Outlined.AccountBox),
    CHAT(category = "Chat", icon = Icons.Outlined.Settings),
    SETTINGS(category = "Settings", icon = Icons.Outlined.Settings),
    PRONUNCIATION(category = "Pronunciation", icon = Icons.Outlined.DateRange),
    BOOKMARKS(category = "Favorites", icon = Icons.Outlined.Star),
    INFO(category = "Info", icon = Icons.Outlined.Info),
    LANGUAGE(category = "Language", icon = Icons.Outlined.Delete)
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
