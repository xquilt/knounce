package com.polendina.knounce.domain.repository

import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations

interface PronunciationsRepository {
    /**
     *  Return the from-to language codes of the respective languages for each available language.
     *
     *  @param word
    */
    fun languageCodes(callback: (response: LanguageCodes?) -> Unit): Unit
    /**
     * Return the available pronunciations for a certain word.
     * Pronunciations returned can be either strictly single words, or entire phrases.
     *
     * @param word The word to return available pronunciations to.
     * @param singleWordEntirePhrase Either return pronunciations of single words or entire phrases. //todo: should be disposed/refined or something
     * @param languageCode
    */
    fun wordPronunciations(word: String, languageCode: String, interfaceLanguageCode: String, callback: (pronunciations: Pronunciations?) -> Unit): Unit
    /**
     *
     *
     */
    fun wordPronunciationsAll(word: String, interfaceLanguageCode: String, callback: (pronunciations: Pronunciations?) -> Unit): Unit
    /**
     *
     *
     */
    fun phrasePronunciations(word: String, languageCode: String, interfaceLanguageCode: String, callback: (pronunciations: Pronunciations?) -> Unit): Unit
    /**
     *
     *
     */
    fun phrasePronunciationsAll(word: String, interfaceLanguageCode: String, callback: (pronunciations: Pronunciations?) -> Unit): Unit
    /**
     * First translate the given word, then obtain the available pronunciations of the translated word,
     *
     * @param word The word to find translation then pronunciation to.
     * @param fromToLanguageCode The from-to language code, that dictates the source and destination language of the translation.
     * @param callback The callback functions, that takes the returned response argument.
    */
    fun translateSearchTranslation(word: String, fromToLanguageCode: String, callback: (response: Pronunciations?) -> Unit): Unit

    /**
     * Obtain a predefined from-to languages list, that displays available translations & pronunciations.
     *
     * @param interfaceLanguage The language of the App's UI.
     * @param callback A callback function, with the Response as a parameter.
     */
    fun translatePronunciationsFromToMap(interfaceLanguage: String, callback: (response: FromToResponse?) -> Unit): Unit
    /**
     * Find available alternative pronunciations of the same word or phrase returned from the general results list.
     *
     * @param wordPhrase The word or the phrase to find alternative pronunciations to.
     * @param languageCode The language code of the desired alternatives. It's usually the language code of the same language of the word/phrase.
     * @param callback The callback code, that takes the returned response as a callback argument.
    */
    fun wordPhraseAlternatives(wordPhrase: String, languageCode: String, callback: (response: Pronunciations?) -> Unit): Unit
    /**
     *
     * @param word The word to look up
     * @return Return the locally saved/cached word, or else return null
    */
    suspend fun loadWordsOfflineFirst(): List<Word>
}