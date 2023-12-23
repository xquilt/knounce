package com.polendina.knounce.presentation.shared.floatingbubble.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import com.polendina.knounce.data.database.Database
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.Pronunciations
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

class FloatingBubbleViewModelMock(
    private val database: Database
): FloatingBubbleViewModel {
    private val LOREM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
    override var srcWord by mutableStateOf(TextFieldValue(text = LOREM_IPSUM))
    override var targetWordDisplay by mutableStateOf(LOREM_IPSUM)

    override var expanded: Boolean by mutableStateOf(true)
    override val words: SnapshotStateList<Word>
//        get() = runBlocking { loadWordsFromDb() }.toMutableStateList()
        get() = emptyList<Word>().toMutableStateList()
    override var currentWord: Word by mutableStateOf(words.random())
    override var pageIndex: Int
        get() = words.indexOf(currentWord)
        set(value) {}

    override fun invertLoaded() {
        currentWord = currentWord.copy(loaded = !currentWord.loaded)
    }

    override fun searchWord(word: String) {
        TODO("Not yet implemented")
    }

    override fun translateWord(word: String): Job {
        TODO("Not yet implemented")
    }

    override suspend fun grabAudioFiles(searchTerm: String): Pronunciations? {
        TODO("Not yet implemented")
    }

    override fun loadPronunciations(word: String): Job {
        TODO("Not yet implemented")
    }

    override fun playAudio(searchTerm: String): Job? {
        TODO("Not yet implemented")
    }
}