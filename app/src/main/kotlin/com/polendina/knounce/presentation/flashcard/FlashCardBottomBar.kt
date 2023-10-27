package com.polendina.knounce.presentation.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class Actions(
    val icon: ImageVector,
    val action: () -> Unit
) {
    Settings(icon = Icons.Default.Settings, action = {}),
    Remove(icon = Icons.Default.PlaylistRemove, action = {}),
    Accept(icon = Icons.Default.Check, action = {}),
    Speaker(icon = Icons.Default.VolumeUp, action = {})
}
@Composable
fun FlashCardBottomBar(
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        MaterialTheme.colorScheme.outline to 30.dp,
        MaterialTheme.colorScheme.error to 50.dp,
        Color.Green to 50.dp,
        MaterialTheme.colorScheme.outline to 30.dp,
    )
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Actions.values().zip(colors).forEach {
            IconButton(
                onClick = it.first.action,
                modifier = Modifier
                    .padding(10.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape
                    )
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape
                    )
                    .padding(10.dp)
                    .size(it.second.second)
            ) {
                Icon(
                    imageVector = it.first.icon,
                    contentDescription = null,
                    tint = it.second.first,
                )
            }
        }
    }
}