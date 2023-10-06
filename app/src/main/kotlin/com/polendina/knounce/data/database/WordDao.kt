package com.polendina.knounce.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert
    suspend fun insertWord(word: WordDb)
    @Query("SELECT * FROM worddb")
    fun getWords(): Flow<List<WordDb>>
}