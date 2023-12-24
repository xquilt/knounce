package com.polendina.knounce.presentation.flashcard.viewmodel

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.polendina.knounce.data.database.Database
import com.polendina.knounce.data.database.Word
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

class FlashCardViewModelMock(
    private val database: Database
): FlashCardViewModel, ViewModel(), Database {
    override val words: SnapshotStateList<Word> = runBlocking { database.loadWordsFromDb().value?.toMutableStateList() ?: emptyList<Word>().toMutableStateList() }
    override fun insertWordToDb(word: Word): Job = database.insertWordToDb(word = word)
    override suspend fun loadWordsFromDb(): LiveData<List<Word>> = database.loadWordsFromDb()
    override fun removeWordFromDb(wordTitle: String): Job = database.removeWordFromDb(wordTitle = wordTitle)
    override var elapsedSeconds by mutableLongStateOf(listOf(0L, 2034L, 60234L, 2342324234L).random())
    init {
        object: CountDownTimer(30000, 1000) {
            override fun onFinish() { }
            override fun onTick(millisUntilFinished: Long) {
                elapsedSeconds = millisUntilFinished / 1000
            }
        }.start()
    }
}