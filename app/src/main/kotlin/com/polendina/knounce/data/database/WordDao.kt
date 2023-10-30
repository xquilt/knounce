package com.polendina.knounce.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDao {
    @Insert
    suspend fun insertWord(word: Word)
    @Query("DELETE FROM word WHERE word.title = :word")
    suspend fun deleteWord(word: String)
    @Query("SELECT * FROM word")
    suspend fun getWords(): List<Word>
}