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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.polendina.knounce.data.database.WordDb
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.Pronunciations

//@Composable
//fun HistoryScreen(
//    viewMode: ViewModel
//) {
//    Scaffold ( ) {
//    }
//}

@Composable
fun HistoryItem(
    word: WordDb
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
            word.pronunciations?.data?.first()?.items?.let {
                LazyRow {
                    items(it) {
                        Text(text = it.word)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItems(
    words: MutableList<WordDb>,
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
    val words = listOf("eingeben", "milch", "wasser", "hallo", "ich", "")
    val wordsDbList = words
        .map {
            WordDb(
                title = it,
                translation = "",
                pronunciations = Pronunciations(
                    status = "",
                    attributes = Pronunciations.Attributes(total = 23, 20),
                    data = listOf(Pronunciations.Datum(
                        language = "English",
                        translation = "Hello",
                        20,
                        items = words.map { Item(
                            id = 0,
                            word = it,
                            original = it,
                            num_pronunciations = "",
                            standard_pronunciation = Gson().fromJson("{\"name\": 10}",
                            JsonElement::class.java)
                        ) }
                    ))
                ),
                id = 0
            )
        }
    HistoryItems(
        words = wordsDbList.toMutableList(),
        modifier = Modifier
            .padding(10.dp)
    )
}