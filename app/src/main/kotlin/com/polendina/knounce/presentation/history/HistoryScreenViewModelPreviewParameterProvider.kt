package com.polendina.knounce.presentation.history

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.polendina.knounce.data.database.DatabaseMock
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.repository.KnounceRepository
import kotlinx.coroutines.runBlocking

internal class HistoryScreenViewModelPreviewParameterProvider :
    PreviewParameterProvider<HistoryScreenViewModel> {
    override val values: Sequence<HistoryScreenViewModel>
        get() {
            return sequenceOf(
                HistoryScreenViewModel(
                    knounceRepository = object : KnounceRepository {
                        override suspend fun wordPronunciations(
                            word: String,
                            languageCode: String,
                            interfaceLanguageCode: String
                        ): Pronunciations? {
                            TODO("Not yet implemented")
                        }

                        override suspend fun loadWords(): List<Word>? {
                            TODO("Not yet implemented")
                        }

                        override suspend fun insertWord(word: Word) {
                            TODO("Not yet implemented")
                        }

                        override suspend fun removeWord(word: String) {
                            TODO("Not yet implemented")
                        }

                        override val words: List<Word>
                            get() = runBlocking { DatabaseMock().loadWordsFromDb() }
                    }
                )
            )
        }
}