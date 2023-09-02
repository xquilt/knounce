package com.polendina.knounce.presentation.pronunciationsscreen.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.presentation.pronunciationsscreen.PronunciationsViewModel
import com.polendina.knounce.presentation.pronunciationsscreen.components.homescreen.PronunciationCard

import androidx.lifecycle.viewmodel.compose.viewModel
import com.polendina.knounce.NetworkHandler
import com.polendina.knounce.R
import com.polendina.knounce.presentation.shared.NoConnectionComposable

@Composable
fun PronunciationsScreen(
    modifier: Modifier = Modifier,
//    pronunciationsViewModel: PronunciationsViewModel = PronunciationsViewModel(ForvoPronunciation()),
    pronunciationsViewModel: PronunciationsViewModel = viewModel(),
    context: Context = LocalContext.current
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
                if (pronunciationsViewModel.networkConnected) {
                    pronunciationsViewModel.showImage = false
                    pronunciationsViewModel.wordPronunciationsAll(it)
                } else {
                    pronunciationsViewModel.languages.clear()
                    pronunciationsViewModel.highlightedLanguages.clear()
                    pronunciationsViewModel.showImage = true
                }
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
        if (pronunciationsViewModel.showImage) {
            NoConnectionComposable(
                onRetryCallback = {
                    // TODO: I'm not sure if I should delegate the searching logic to the view, because I can't just copy paste the previous code down here. I guess the ViewModel search function is just useless one, and should entail more logic to have meaning in life.
                },
                modifier = Modifier
                    .height(600.dp)
                    .padding(
                        horizontal = 50.dp
                    )
            )
        }
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
//                            if (NetworkHandler(context).isNetworkAvailable()) {
                            if (pronunciationsViewModel.networkConnected) {
                                PronunciationPlayer.playRemoteAudio(it)
                            } else {
                                Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show()
                            }
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
