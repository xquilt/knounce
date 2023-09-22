package com.polendina.knounce.data.repository.pronunciation

import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.presentation.shared.floatingbubble.FORVO_LANGUAGE
import com.polendina.knounce.presentation.shared.floatingbubble.FloatingBubbleViewModel
import kotlinx.coroutines.runBlocking
import me.bush.translator.Language
import me.bush.translator.Translator

import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trancore.corelib.pronunciation.retrofitInstance
import kotlin.random.Random

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
            listOf("sieh dir an, was du verpasst \n noch", "einem", "nacht", "bett", "daddy", "@!#$@!#$", "inckognitotab", "eingaben").map { it.replace("\n", "") }.forEach {word ->
                println(word)
                listOf(false, true, true, true, true, true).forEach { choice ->
                    println(FloatingBubbleViewModel().grabAudioFile(
                        searchTerm = word,
                        shuffle = choice
                    ))
                }
                println()
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