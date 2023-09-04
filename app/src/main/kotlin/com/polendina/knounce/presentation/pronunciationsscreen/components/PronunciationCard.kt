package com.polendina.knounce.presentation.pronunciationsscreen.components.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.polendina.knounce.domain.model.Item

@Composable
internal fun PronunciationCard(
    pronunciation: Item,
    onRecordButtonCallback: () -> Unit,
    onPlayButtonClickCallback: (pronunciation: Item) -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 5.dp)
            .height(60.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pronunciation.standard_pronunciation.isJsonObject) {
                IconButton(
                    onClick = {
                        onPlayButtonClickCallback( pronunciation )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        tint = Color.Blue
                    )
                }
            } else {
                IconButton(
                    onClick = onRecordButtonCallback
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
            Column {
                Text(text = pronunciation.original, style = TextStyle(
                    fontWeight = FontWeight.SemiBold
                )
                )
                Text(
                    text = pronunciation.num_pronunciations,
                    style = TextStyle(
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }
}