package com.polendina.knounce.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(
    entities = [WordDb::class],
    version = 1,
    exportSchema = false
)
abstract class WordDatabase: RoomDatabase() {
    abstract val wordDao: WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordDatabase? = null
        fun getDatabase(context: Context): WordDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = WordDatabase::class.java,
                    name = "words"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}