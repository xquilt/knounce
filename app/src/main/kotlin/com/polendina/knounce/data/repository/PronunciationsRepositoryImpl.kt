package com.polendina.knounce.data.repository

import com.polendina.knounce.data.database.Word
import com.polendina.knounce.data.database.WordDao
import com.polendina.knounce.data.network.ForvoService
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.domain.repository.PronunciationsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PronunciationsRepositoryImpl(
    private val forvoService: ForvoService,
    private val wordDao: WordDao
): PronunciationsRepository {
    override fun languageCodes(callback: (response: LanguageCodes?) -> Unit) {
        forvoService.languageCodes(UserLanguages.ENGLISH.code).enqueue(object: Callback<LanguageCodes> {
            override fun onResponse(call: Call<LanguageCodes>, response: Response<LanguageCodes>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<LanguageCodes>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    override fun wordPronunciations(
        word: String,
        languageCode: String,
        interfaceLanguageCode: String,
        callback: (pronunciations: Pronunciations?) -> Unit
    ) {
        forvoService.wordPronunciations(
            word = word,
            languageCode = languageCode,
            interfaceLanguageCode = interfaceLanguageCode
        ).enqueue(object: Callback<Pronunciations> {
            override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    override fun wordPronunciationsAll(
        word: String,
        interfaceLanguageCode: String,
        callback: (pronunciations: Pronunciations?) -> Unit
    ) {
        forvoService.wordPronunciationsAll(
            word = word,
            interfaceLanguageCode = interfaceLanguageCode
        ).enqueue(object: Callback<Pronunciations> {
            override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    override fun phrasePronunciations(
        word: String,
        languageCode: String,
        interfaceLanguageCode: String,
        callback: (pronunciations: Pronunciations?) -> Unit
    ) {
        forvoService.phrasePronunciations(
            word = word,
            languageCode = languageCode,
            interfaceLanguageCode = interfaceLanguageCode
        ).enqueue(object:
            Callback<Pronunciations> {
            override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun phrasePronunciationsAll(
        word: String,
        interfaceLanguageCode: String,
        callback: (pronunciations: Pronunciations?) -> Unit
    ) {
        forvoService.phrasePronunciationsAll(
            word = word,
            interfaceLanguageCode = interfaceLanguageCode
        ).enqueue(object:
            Callback<Pronunciations> {
            override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun translatePronunciationsFromToMap(
        interfaceLanguage: String,
        callback: (response: FromToResponse?) -> Unit
    ) {
        forvoService.pronunciationTranslationMap(interfaceLanguage).enqueue(object: Callback<FromToResponse> {
            override fun onResponse(call: Call<FromToResponse>, response: Response<FromToResponse>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<FromToResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun translateSearchTranslation(
        word: String,
        fromToLanguageCode: String,
        callback: (response: Pronunciations?) -> Unit
    ): Unit {
        forvoService.searchTranslation(
            word = word,
            fromToLanguageCode = fromToLanguageCode,
            languageCode = UserLanguages.FRANCAIS.code
        ).enqueue(object: Callback<Pronunciations> {
            override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    override fun wordPhraseAlternatives(
        wordPhrase: String,
        languageCode: String,
        callback: (response: Pronunciations?) -> Unit
    ): Unit {
        forvoService.alternativePronunciations(
            wordPhrase = wordPhrase,
            languageCode = languageCode
        ).enqueue(object: Callback<Pronunciations> {
            override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    override suspend fun loadWordsOfflineFirst(): List<Word> {
        return wordDao.getWords()
    }
}