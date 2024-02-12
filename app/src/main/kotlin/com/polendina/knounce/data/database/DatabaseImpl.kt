package com.polendina.knounce.data.database

import android.app.Application
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseImpl (
    application: Application
): Database {
    private val database: WordDatabase by lazy { WordDatabase.getDatabase(application) }
    private val wordDao = database.wordDao
    private val ioScope = CoroutineScope(Dispatchers.IO)

    /**
     * load words from the local Room database.
     *
     * @return Return a List of Words.
     */
    override suspend fun loadWordsFromDb(): List<Word> = wordDao.getWords()
    // TODO: Maybe that logic can be encapsulated to a separate extension function!
//        .map {
//            Word(
//                title = it.title,
//                translation = it.translation,
//                pronunciations = it.pronunciations?.toMutableStateList(),
//                loaded = it.loaded
//            )
//        }

    override fun insertWordToDb(word: Word) = ioScope.launch {
        wordDao.insertWord(Word(
            title = word.title,
            translation = word.translation?.toMutableMap(),
            pronunciations = word.pronunciations,
            loaded = true
        ))
    }

    /**
     * Remove a word from the database, but not from the current viewModel list.
     *
     * @param wordTitle The Word title to be delete. Obtained from moe current viewmodel.
     */
    override fun removeWordFromDb(wordTitle: String) = ioScope.launch {
        wordDao.deleteWord(wordTitle)
    }

}