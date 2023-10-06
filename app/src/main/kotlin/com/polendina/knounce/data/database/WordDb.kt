package com.polendina.knounce.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.polendina.knounce.domain.model.Pronunciations

@Entity
data class WordDb(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "translation")
    val translation: String,
    @ColumnInfo(name = "pronunciations")
    val pronunciations: Pronunciations?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int
)