package com.polendina.knounce.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromTranslationToString(translation: MutableMap<String, MutableList<Word.Translation>>) =
        Gson().toJson(translation)

    @TypeConverter
    fun fromStringToTranslation(translation: String): MutableMap<String, MutableList<Word.Translation>> =
        Gson().fromJson(
            translation,
            object : TypeToken<MutableMap<String, MutableList<Word.Translation>>>() {}.type
        )

    @TypeConverter
    fun fromPronunciationsToString(pronunciations: MutableList<Pair<String, String>>) =
        Gson().toJson(pronunciations)

    @TypeConverter
    fun fromStringToPronunciations(pronunciations: String): MutableList<Pair<String, String>> =
        Gson().fromJson(
            pronunciations,
            object : TypeToken<List<Pair<String, String>>>() {}.type
        )
}
