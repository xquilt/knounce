package com.polendina.knounce.data.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DatabaseMock: Database {
    private val _words = listOf("eingeben", "milch", "wasser", "hallo", "ich", "")
        .map {
            Word(
                title = it,
                translation = listOf("Adjective", "Noun", "Adverb", "Preposition").map {
                    it to
                    (0..10).map {
                        Word.Translation(
                            explanation = listOf("More", "or", "less", "gaben", "kafka", "gai")
                                .shuffled()
                                .joinToString(" "),
                            examples = (1..10).map {
                                listOf(
                                    "more",
                                    "gaben",
                                    "ich",
                                    "hai",
                                    "kanka",
                                    "gestern"
                                ).shuffled().joinToString(" ")
                            }
                        )
                    }.toMutableList()
//                    mutableListOf(
//                    )
                }.toMap().toMutableMap(),
                pronunciations = null,
                loaded = true
            )
        }
    private val ioScope = CoroutineScope(Dispatchers.IO)
    override suspend fun loadWordsFromDb(): List<Word> = _words

    override fun insertWordToDb(word: Word): Job = ioScope.launch {}

    override fun removeWordFromDb(wordTitle: String): Job = ioScope.launch {}
}