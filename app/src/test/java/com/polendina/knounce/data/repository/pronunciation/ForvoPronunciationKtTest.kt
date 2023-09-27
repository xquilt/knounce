package com.polendina.knounce.data.repository.pronunciation

import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.presentation.shared.floatingbubble.FloatingBubbleViewModel
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
import trancore.corelib.pronunciation.retrofitInstance

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
            words.map { it.first } .map { it.replace("\n", "") }.forEach {word ->
                println(word)
                listOf(false, true, true, true, true, true).forEach { choice ->
                    println(FloatingBubbleViewModel().grabAudioFiles(
                        searchTerm = word
                    ))
                }
                println()
            }
        }
    }

}

class GoogleTranslationTest {

    private lateinit var floatingBubbleViewModel: FloatingBubbleViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        floatingBubbleViewModel = FloatingBubbleViewModel()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun translateWordTest() = runTest {
        words.map {
            floatingBubbleViewModel.srcWordDisplay = it.first
            floatingBubbleViewModel.translateWord().join()
            assert(it.second == floatingBubbleViewModel.targetWordDisplay)
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