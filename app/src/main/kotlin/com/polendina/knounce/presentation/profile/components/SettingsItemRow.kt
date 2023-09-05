package com.polendina.knounce.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingItemRow(
    settingsItem: SettingsItem,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .height(80.dp)
            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = settingsItem.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .notificationCircle()
                )
                Spacer(modifier = Modifier.padding(15.dp))
                Text(
                    text = settingsItem.category
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .notificationRect()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingItemRowPreview() {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        SettingItemRow(SettingsItem.ACCOUNT)
        SettingItemRow(SettingsItem.SETTINGS)
    }
}