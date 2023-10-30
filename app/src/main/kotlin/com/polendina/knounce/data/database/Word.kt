package com.polendina.knounce.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "translation")
    var translation: MutableMap<String, MutableList<Translation>>?,
    @ColumnInfo(name = "pronunciations")
    var pronunciations: MutableList<Pair<String, String>>?,
    @ColumnInfo(name = "loaded")
    var loaded: Boolean,
) {
    data class Translation(
        val explanation: String?,
        val examples: List<String>?
    )
}