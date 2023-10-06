package com.polendina.knounce.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.polendina.knounce.domain.model.Pronunciations

class Converters {
    @TypeConverter
    fun fromPronunciationsToString(pronunciations: Pronunciations?) = Gson().toJson(pronunciations)
    @TypeConverter
    fun fromStringToPronunciations(pronunciations: String) = Gson().fromJson(pronunciations, Pronunciations::class.java)
}
