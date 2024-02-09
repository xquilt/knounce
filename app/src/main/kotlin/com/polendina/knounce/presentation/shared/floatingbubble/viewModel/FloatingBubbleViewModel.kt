package com.polendina.knounce.presentation.shared.floatingbubble.viewModel

import androidx.compose.ui.text.input.TextFieldValue
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.Pronunciations
import kotlinx.coroutines.Job

interface FloatingBubbleViewModel {
    var srcWord: TextFieldValue
    var targetWordDisplay: String
    var expanded: Boolean
    val words: List<Word>
    var currentWord: Word
    var pageIndex: Int
    fun invertLoaded()
    fun searchWord(word: String)
    suspend fun translateWord(word: String)
    suspend fun grabAudioFiles(searchTerm: String): Pronunciations?
    suspend fun loadPronunciations(word: String)
    fun playAudio(searchTerm: String): Job?
    fun insertWord(word: Word): Job
    fun removeWord(word: String): Job
}