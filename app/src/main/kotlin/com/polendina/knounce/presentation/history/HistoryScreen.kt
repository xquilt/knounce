package com.polendina.knounce.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.data.database.DatabaseMock
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.presentation.flashcard.viewmodel.FlashCardViewModelMock

@Composable
fun HistoryItem(
    word: Word
) {
    Box (
        modifier = Modifier
            .height(100.dp)
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

@Composable
fun HistoryItems(
    words: SnapshotStateList<Word>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(
            words
        ) {
            HistoryItem(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryItemsPreview(

) {
    HistoryItems(
        // TODO: The following viewmodel should be renamed to something more appropriate.
        words = FlashCardViewModelMock(database = DatabaseMock()).words,
        modifier = Modifier
            .padding(10.dp)
    )
}