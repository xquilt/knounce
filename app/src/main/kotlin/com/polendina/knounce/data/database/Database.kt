package com.polendina.knounce.data.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Job

interface Database {
    suspend fun loadWordsFromDb(): LiveData<List<Word>>
    fun insertWordToDb(word: Word): Job
    fun removeWordFromDb(wordTitle: String): Job
}