package com.polendina.knounce.data.repository.pronunciation

import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.presentation.shared.floatingbubble.FORVO_LANGUAGE
import kotlinx.coroutines.runBlocking
import me.bush.translator.Language
import me.bush.translator.Translator

import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trancore.corelib.pronunciation.retrofitInstance

class ForvoPronunciationKtTest {

    @Test
    fun addTwo() {
        assert(addTwo(1, 2).equals(3))
    }

    @Test
    fun getNotes() {
        ForvoPronunciation.wordPronunciationsAll(
            word = "game",
            interfaceLanguageCode = UserLanguages.ENGLISH.code
        ) {
            println("Hello world")
            println(it?.data)
        }
        println("Hello world")
    }

    @Test
    fun wordPronunciationsAll() {
        listOf("collaboration", "talk", "gaming", "game").forEach {
            retrofitInstance.wordPronunciationsAll(it, UserLanguages.ENGLISH.code).execute().run {
                println(body())
            }
        }
    }

    @Test
    fun translatePronunciationsFromToMap() {
        retrofitInstance.pronunciationTranslationMap(
            UserLanguages.ENGLISH.code
        ).execute().let {
            if (it.isSuccessful) {
                it.body()?.let {
                    if(it.status == "ok") {
                        it.response.forEach {
                            println(it.children)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun firstPronunciation() {
        runBlocking {
            listOf("einem", "nacht", "bett", "daddy").forEach {
                retrofitInstance.wordPronunciations(
                    word = it,
                    interfaceLanguageCode = UserLanguages.ENGLISH.code,
                    languageCode = FORVO_LANGUAGE.GERMAN.code
                ).execute().let {
                    try {
                        Gson().fromJson(it.body()?.data?.first()?.items?.first()?.standard_pronunciation, Item.StandardPronunciation::class.java).run {
                            println(this.realmp3)
                        }
                    } catch (e: NoSuchElementException) {}
                }
            }
        }
    }

}

class GoogleTranslationTest {

    @Test
    fun wordTranslatorTest() {
        runBlocking {
            listOf("game", "Hello", "welcome").forEach {
                Translator().translate(
                    text = it,
                    source = Language.AUTO,
                    target = Language.GERMAN,
                ).apply {
                    println(this.translatedText)
                }
            }
        }
    }

}