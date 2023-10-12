package com.polendina.knounce.domain.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Word(
    title: String = "",
    translation: String = "",
    pronunciations: Pronunciations? = null,
    val id: Int = 0
) {
    var title by mutableStateOf(title)
    var translation by mutableStateOf(translation)
    var pronunciations by mutableStateOf(pronunciations)
}