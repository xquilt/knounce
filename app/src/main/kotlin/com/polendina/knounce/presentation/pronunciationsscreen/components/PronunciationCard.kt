package com.polendina.knounce.presentation.pronunciationsscreen.components.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import com.polendina.knounce.domain.model.Pronunciations

@Composable
internal fun PronunciationCard(
    pronunciation: Pronunciations.Datum.Item,
    onPlayButtonClickCallback: (mediaUrl: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .height(60.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onPlayButtonClickCallback(pronunciation.standard_pronunciation.realmp3)
                }
            ) {
                Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = null, tint = Color.Blue)
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

