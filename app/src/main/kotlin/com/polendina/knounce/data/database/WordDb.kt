package com.polendina.knounce.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.polendina.knounce.domain.model.Pronunciations

@Entity
data class WordDb(
    @PrimaryKey
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "translation")
    val translation: String, // TODO: This will need to be refactored to another more intricate/all-encompassing data class.
    @ColumnInfo(name = "pronunciations")
    val pronunciations: Pronunciations?, // TODO: This will need to be refactored to another data class for pronunciations (to allow multiple sources with varying json responses)
    @ColumnInfo(name = "loaded")
    val loaded: Boolean,
)