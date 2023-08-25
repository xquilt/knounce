package com.polendina.knounce.presentation.pronunciationsscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.presentation.pronunciationsscreen.PronunciationsViewModel
import com.polendina.knounce.presentation.pronunciationsscreen.components.homescreen.PronunciationCard

import androidx.lifecycle.viewmodel.compose.viewModel
import com.polendina.knounce.presentation.pronunciationsscreen.pronunciations

@Composable
fun PronunciationsScreen(
    modifier: Modifier = Modifier,
//    pronunciationsViewModel: PronunciationsViewModel = PronunciationsViewModel(ForvoPronunciation()),
    pronunciationsViewModel: PronunciationsViewModel = viewModel(),
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        SearchField(
            query = pronunciationsViewModel.query.value,
            onQueryChange = {
                pronunciationsViewModel.updateQuery(it)
            },
            onActiveChange = {},
            onSearch = {
                pronunciationsViewModel.wordPronunciationsAll(it)
            },
            onTrailingIconClick = { /*TODO*/ },
            modifier = Modifier
                .padding(vertical = 10.dp)
                .height(70.dp)
        )
        LanguagesRow(
            languages = pronunciationsViewModel.highlightedLanguages,
            onLanguageBoxClick = {
                pronunciationsViewModel.filterPronunciations(it)
            }
        )
        LazyColumn {
            pronunciationsViewModel.highlightedLanguages.filter { it.selected }.run {
                if (isEmpty()) pronunciationsViewModel.languages else pronunciationsViewModel.languages.filter {
                    this.map { it.name }.contains(it.language)
                }
            }.forEach {
                items(it.items) {
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

}

@Preview(showBackground = true)
@Composable
fun PronunciationsScreenPreview() {
    PronunciationsScreen()
}
