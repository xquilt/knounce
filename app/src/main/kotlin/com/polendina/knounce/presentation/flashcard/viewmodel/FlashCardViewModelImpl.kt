package com.polendina.knounce.presentation.flashcard.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.polendina.knounce.data.database.Database
import com.polendina.knounce.data.database.Word
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FlashCardViewModelImpl(
    private val application: Application,
    private val database: Database
): FlashCardViewModel, AndroidViewModel(application),
    Database {
    override var words: SnapshotStateList<Word> = mutableStateListOf()

    init {
        viewModelScope.launch {
            words.addAll(database.loadWordsFromDb())
        }
    }

    override fun insertWordToDb(word: Word): Job = database.insertWordToDb(word = word)
    override suspend fun loadWordsFromDb(): List<Word> = database.loadWordsFromDb()
    override fun removeWordFromDb(wordTitle: String): Job =
        database.removeWordFromDb(wordTitle = wordTitle)

    override var elapsedSeconds: Long
        get() = 0L
        set(value) {}
}