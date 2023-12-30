package com.polendina.knounce.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.polendina.knounce.data.database.Word

@Composable
fun HistoryItem(
    word: Word
) {
    Box (
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = word.title)
            LazyRow {
                items(word.pronunciations ?: mutableListOf()) {
                    Text(text = it.second)
                }
            }
        }
    }
}