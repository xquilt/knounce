package com.polendina.knounce.presentation.shared.floatingbubble.viewModel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import com.polendina.knounce.data.database.Database
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.Pronunciations
import kotlinx.coroutines.Job

interface FloatingBubbleViewModel: Database {
    var srcWord: TextFieldValue
    var targetWordDisplay: String
    var expanded: Boolean
    val words: SnapshotStateList<Word>
    var currentWord: Word
    var pageIndex: Int
    fun invertLoaded(): Unit
    fun searchWord(word: String)
    fun translateWord(word: String): Job
    suspend fun grabAudioFiles(searchTerm: String): Pronunciations?
    fun loadPronunciations(word: String): Job
    fun playAudio(searchTerm: String): Job?
}