package com.polendina.knounce.data.repository.pronunciation

import android.app.Application
import com.polendina.knounce.data.database.DatabaseImpl
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.presentation.shared.floatingbubble.FloatingBubbleViewModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import me.bush.translator.Language
import me.bush.translator.Translator
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import trancore.corelib.pronunciation.retrofit

private val words = listOf(
    Pair("einem", "one"),
    Pair("nacht", "night"),
    Pair("daddy", "daddy"),
    Pair("@!#$@!#$", "@!#$@!#$"),
    Pair("inckognitotab", "incognito tab"),
    Pair("eingaben", "inputs"),
    Pair("sieh dir an, was du verpasst", "see what you're missing")
)

class ForvoPronunciationKtTest {

    private val application = Mockito.mock(Application::class.java)

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
            retrofit.wordPronunciationsAll(it, UserLanguages.ENGLISH.code).execute().run {
                println(body())
            }
        }
    }

    @Test
    fun translatePronunciationsFromToMap() {
        retrofit.pronunciationTranslationMap(
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
            words.map { it.first } .map { it.replace("\n", "") }.forEach {word ->
                println(word)
                listOf(false, true, true, true, true, true).forEach { choice ->
                    println(FloatingBubbleViewModelImpl(application = application, database = DatabaseImpl()).grabAudioFiles(
                        searchTerm = word
                    ))
                }
            }
        }
    }

}

class GoogleTranslationTest {

    private lateinit var floatingBubbleViewModelImpl: FloatingBubbleViewModelImpl
    private val application = Mockito.mock(Application::class.java)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        floatingBubbleViewModelImpl = FloatingBubbleViewModelImpl(application)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun translateWordTest() = runTest {
        words.map {
            floatingBubbleViewModelImpl.currentWord = Word(it.first, null, null, false)
            floatingBubbleViewModelImpl.translateWord().join()
            assert(it.second == floatingBubbleViewModelImpl.targetWordDisplay)
        }
    }

    @Test
    fun wordTranslatorTest() = runTest {
        println(words.map { it.first }.map {
            Pair(
                it,
                Translator().translate(
                    text = it,
                    source = Language.GERMAN,
                    target = Language.ENGLISH,
                ).translatedText
            )
        })
    }
}