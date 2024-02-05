package com.polendina.knounce.presentation.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.data.database.DatabaseMock
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.presentation.flashcard.viewmodel.FlashCardViewModelMock

@Composable
fun HistoryItems(
    words: SnapshotStateList<Word>,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var count by remember { mutableIntStateOf(words.size) }
    Scaffold (
        topBar = {
            HistoryScreenTopBar(
                searchQuery = query,
                onSearchQueryChange = { query = it },
                moreCallback = {},
                count = count,
                navigateCallback = {}
            )
        },
        modifier = modifier
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(it)
        ) {
            items(items = words) {
                HistoryItem(
                   word = it,
                    moreCallback = { }
                )
            }
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
            .padding(
//                vertical = 20.dp,
                horizontal = 5.dp
            )
    )
}