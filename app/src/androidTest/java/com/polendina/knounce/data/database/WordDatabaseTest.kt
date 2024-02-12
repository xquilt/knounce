package com.polendina.knounce.data.database

import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WordDatabaseTest {

    private lateinit var wordDao: WordDao
    @Before
    fun setup() {
        val wordDatabase: WordDatabase by lazy { WordDatabase.getDatabase(ApplicationProvider.getApplicationContext()) }
        wordDao = wordDatabase.wordDao
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWordDao() = runTest {
        val words = wordDao.getWords()
        advanceUntilIdle()
        assertEquals(
            listOf("what"),
            words
        )
    }

    @Test
    fun deleteWord() = runTest {
        listOf("nich", "nicht").forEach {
            wordDao.insertWord(Word(title = it, translation = null,pronunciations = null, loaded = false))
        }
        wordDao.deleteWord("nicht")
        assertEquals(
            listOf("nich"),
            wordDao.getWords().value?.map { it.title }
        )
    }

}