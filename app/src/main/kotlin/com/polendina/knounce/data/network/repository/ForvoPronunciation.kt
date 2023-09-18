package com.polendina.knounce.data.repository.pronunciation

import android.util.Log
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.domain.repository.PronunciationRepository
import org.slf4j.event.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trancore.corelib.pronunciation.retrofitInstance
import java.util.logging.Logger

fun addTwo(one: Int, two: Int): Int {
    return (one + two)
}

object ForvoPronunciation: PronunciationRepository {
    override fun languageCodes(callback: (response: LanguageCodes?) -> Unit) {
        retrofitInstance.languageCodes(UserLanguages.ENGLISH.code).enqueue(object: Callback<LanguageCodes> {
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
        retrofitInstance.wordPronunciations(
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
            }
        )
    }
    override fun wordPronunciationsAll(
        word: String,
        interfaceLanguageCode: String,
        callback: (pronunciations: Pronunciations?) -> Unit
    ) {
        retrofitInstance.wordPronunciationsAll(
            word = word,
            interfaceLanguageCode = interfaceLanguageCode
        ).enqueue(object: Callback<Pronunciations> {
                override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
                    callback(response.body())
                }
                override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }
    override fun phrasePronunciations(
        word: String,
        languageCode: String,
        interfaceLanguageCode: String,
        callback: (pronunciations: Pronunciations?) -> Unit
    ) {
        retrofitInstance.phrasePronunciations(
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
        retrofitInstance.phrasePronunciationsAll(
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
        retrofitInstance.pronunciationTranslationMap(interfaceLanguage).enqueue(object: Callback<FromToResponse> {
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
        retrofitInstance.searchTranslation(
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
        retrofitInstance.alternativePronunciations(
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

}