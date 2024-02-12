package com.polendina.knounce.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A mocking class for a database
 *
 * @param emptyWords Whether or not it shouldn't load words.
 * @param emptyExplanations Whether or not it shouldn't load explanation.
 * @param emptyExamples Whether or not it shouldn't load examples.
 */
class DatabaseMock(
    val emptyWords: Boolean = false,
    val emptyExplanations: Boolean = false,
    val emptyExamples: Boolean = false,
    val emptyPronunciations: Boolean = false
): Database {
    private val _words = listOf("more", "gaben", "ich", "hai", "kanka", "gestern", "gaben", "ich", "hai")
        .run { if (emptyWords) take(0) else this }
        .map {
            Word(
                title = it,
                translation = listOf("Adjective", "Noun", "Adverb", "Preposition").map {
                    it to
                    (1..10).map {
                        Word.Translation(
                            explanation = listOf("More", "or", "less", "gaben", "kafka", "gai", "gaben", "ich", "kane", "heins")
                                .run { if (emptyExplanations) take(0) else this }
                                .shuffled()
                                .joinToString(" "),
                            examples = (1..10).map {
                                listOf(
                                    "more",
                                    "gaben",
                                    "ich",
                                    "hai",
                                    "kanka",
                                    "gestern",
                                    "gaben",
                                    "ich",
                                    "hai",
                                )
                                .run { if (emptyExamples) take(0) else this }
                                .shuffled().joinToString(" ")
                            }
                        )
                    }.toMutableList()
                }.toMap().toMutableMap(),
                pronunciations = listOf("gaben", "ich", "hai", "kanka", "gestern", "haben")
                    .map { it to ""}
                    .run { if (emptyPronunciations) take(0) else this }
                    .toMutableList(),
                loaded = true
            )
        }
    private val ioScope = CoroutineScope(Dispatchers.IO)
    override suspend fun loadWordsFromDb(): List<Word> = _words

    override fun insertWordToDb(word: Word): Job = ioScope.launch {}

    override fun removeWordFromDb(wordTitle: String): Job = ioScope.launch {}
}