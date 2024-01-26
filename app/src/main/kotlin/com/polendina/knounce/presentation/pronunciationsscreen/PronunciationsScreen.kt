package com.polendina.knounce.presentation.pronunciationsscreen.components

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Switch
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.polendina.knounce.presentation.pronunciationsscreen.PronunciationsViewModel
import com.polendina.knounce.presentation.pronunciationsscreen.components.homescreen.PronunciationCard

import androidx.lifecycle.viewmodel.compose.viewModel
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.data.network.ForvoService
import com.polendina.knounce.data.repository.PronunciationsRepositoryImpl
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.repository.PronunciationsRepository
import com.polendina.knounce.presentation.pronunciationsscreen.pronunciationsRepositoryImpl
import com.polendina.knounce.presentation.shared.NoConnectionComposable

@Composable
fun PronunciationsScreen(
    pronunciationsViewModel: PronunciationsViewModel = viewModel(),
    context: Context = LocalContext.current,
    switchEnabled: Boolean = false,
    toggleHover: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold (
        topBar = {
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
                    .padding(
                        vertical = 10.dp,
                        horizontal = 10.dp
                    )
                    .height(70.dp)
            )
        }
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(10.dp)
        ) {
            Switch(checked = switchEnabled, onCheckedChange = toggleHover)
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
                                pronunciationsViewModel.playRemoteAudio(it).run {
                                    if (!this) Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onRecordButtonCallback = {
                                // TODO: I have no idea what should be done in case of pressing 'record', and if even that's a feature at the original app.
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PronunciationsScreenPreview() {
    PronunciationsScreen(
        toggleHover = {},
        pronunciationsViewModel = PronunciationsViewModel(
            pronunciationRepository = object: PronunciationsRepository {
                override fun languageCodes(callback: (response: LanguageCodes?) -> Unit) {
                    TODO("Not yet implemented")
                }
                override fun wordPronunciations(
                    word: String,
                    languageCode: String,
                    interfaceLanguageCode: String,
                    callback: (pronunciations: Pronunciations?) -> Unit
                ) {
                    TODO("Not yet implemented")
                }

                override fun wordPronunciationsAll(
                    word: String,
                    interfaceLanguageCode: String,
                    callback: (pronunciations: Pronunciations?) -> Unit
                ) {
                    TODO("Not yet implemented")
                }

                override fun phrasePronunciations(
                    word: String,
                    languageCode: String,
                    interfaceLanguageCode: String,
                    callback: (pronunciations: Pronunciations?) -> Unit
                ) {
                    TODO("Not yet implemented")
                }

                override fun phrasePronunciationsAll(
                    word: String,
                    interfaceLanguageCode: String,
                    callback: (pronunciations: Pronunciations?) -> Unit
                ) {
                    TODO("Not yet implemented")
                }

                override fun translateSearchTranslation(
                    word: String,
                    fromToLanguageCode: String,
                    callback: (response: Pronunciations?) -> Unit
                ) {
                    TODO("Not yet implemented")
                }

                override fun translatePronunciationsFromToMap(
                    interfaceLanguage: String,
                    callback: (response: FromToResponse?) -> Unit
                ) {
                    TODO("Not yet implemented")
                }

                override fun wordPhraseAlternatives(
                    wordPhrase: String,
                    languageCode: String,
                    callback: (response: Pronunciations?) -> Unit
                ) {
                    TODO("Not yet implemented")
                }

                override suspend fun loadWords() {
                    TODO("Not yet implemented")
                }

                override suspend fun insertWord(word: Word) {
                    TODO("Not yet implemented")
                }

                override suspend fun removeWord(word: String) {
                    TODO("Not yet implemented")
                }

                override val words: LiveData<List<Word>>
                    get() = TODO("Not yet implemented")
            }
        )
    )
}
