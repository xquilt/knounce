package com.polendina.knounce.domain.repository

import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.Pronunciations

interface KnounceRepository {
    /**
     * Return the available pronunciations for a certain word.
     * Pronunciations returned can be either strictly single words, or entire phrases.
     *
     * @param word The word to return available pronunciations to.
     * @param languageCode The language code of the destination language to be looked up
     */
    suspend fun wordPronunciations(
        word: String,
        languageCode: String,
        interfaceLanguageCode: String,
    ): Pronunciations?

    /**
     *
     * @return Return the list of words that are either saved up locally or fetched from the network.
     */
    suspend fun loadWords(): List<Word>?

    /**
     *
     * @param word
     */
    suspend fun insertWord(word: Word)
    /**
     *
     * @param word
     */
    suspend fun removeWord(word: String)

    /**
     *
     * The remotely fetched words or the locally saved/cached words.
     */
    // TODO: Better be a variable of type Flow or liveData
    val words: List<Word>
}