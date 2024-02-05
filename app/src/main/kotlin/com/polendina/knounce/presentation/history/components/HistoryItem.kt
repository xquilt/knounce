package com.polendina.knounce.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.polendina.knounce.data.database.Word

@Composable
fun HistoryItem(
    word: Word,
    moreCallback: (Word) -> Unit
) {
    Box(
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = word.title
                )
                Text(
                    text = word.translation?.values?.first()?.first()?.explanation ?: ""
                )
                LazyRow {
                    items(word.pronunciations ?: mutableListOf()) {
                        Text(text = it.second)
                    }
                }
            }
            IconButton(
                onClick = {
                    moreCallback(word)
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
        }
    }
}