package com.polendina.knounce.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@AutoMigration(from = 1, to = 2)
@TypeConverters(Converters::class)
@Database(
    entities = [Word::class],
    version = 2,
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
                    name = "word"
                )
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}