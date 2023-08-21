package com.polendina.knounce.presentation.pronunciationsscreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.data.repository.pronunciation.ForvoPronunciation
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.repository.PronunciationRepository
import com.polendina.knounce.presentation.pronunciationsscreen.components.homescreen.PronunciationCard

@Composable
fun PronunciationsScreen(
    modifier: Modifier = Modifier,
    pronunciationRepository: PronunciationRepository
) {
    var query by remember { mutableStateOf("") }
    val pronunciationsList = remember { mutableStateListOf<Pronunciations.Datum.Item>() }
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchField(
            onActiveChange = {},
            onSearch = {
                pronunciationRepository.wordPronunciations(it) {
                    it?.apply {
                        pronunciationsList.clear()
                        it.data.forEach {
                            pronunciationsList.addAll(it.items)
                        }
                    }
                }
            },
            query = query,
            onQueryChange = {
                query = it
            },
            onTrailingIconClick = { /*TODO*/ },
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        LazyColumn {
            items(pronunciationsList) {
                PronunciationCard(
                    pronunciation = it,
                    onPlayButtonClickCallback = {
                        PronunciationPlayer.playRemoteAudio(it)
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PronunciationsScreenPreview() {
    PronunciationsScreen(pronunciationRepository = ForvoPronunciation())
}
