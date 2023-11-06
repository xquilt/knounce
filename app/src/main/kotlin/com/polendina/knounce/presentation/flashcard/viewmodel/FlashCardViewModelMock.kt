package com.polendina.knounce.presentation.flashcard.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.polendina.knounce.data.database.Database
import com.polendina.knounce.data.database.Word
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

class FlashCardViewModelMock(
    private val database: Database
): FlashCardViewModel, ViewModel(), Database {
    override val words: SnapshotStateList<Word> = runBlocking { database.loadWordsFromDb().toMutableStateList() }
    override fun insertWordToDb(word: Word): Job = database.insertWordToDb(word = word)
    override suspend fun loadWordsFromDb(): List<Word> = database.loadWordsFromDb()
    override fun removeWordFromDb(wordTitle: String): Job = database.removeWordFromDb(wordTitle = wordTitle)
}