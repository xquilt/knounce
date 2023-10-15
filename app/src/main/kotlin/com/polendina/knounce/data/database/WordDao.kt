package com.polendina.knounce.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDao {
    @Insert
    suspend fun insertWord(word: WordDb)
    @Query("DELETE FROM worddb WHERE worddb.title = :word")
    suspend fun deleteWord(word: String)
    @Query("SELECT * FROM worddb")
    suspend fun getWords(): List<WordDb>
}