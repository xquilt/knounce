package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import com.polendina.knounce.domain.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.mockito.Mockito

class FloatingBubbleViewModelTest {

    private val application = Mockito.mock(Application::class.java)
    private lateinit var floatingBubbleViewModel: FloatingBubbleViewModel
    private var germanWords = listOf(
        Word(
            title = "nacht",
            translation = "night",
            pronunciations = null
        ),
        Word(
            title = "milch",
            translation = "milk",
            pronunciations = null
        ),
        Word(
            title = "ich",
            translation = "I",
            pronunciations = null
        ),
        Word(
            title = "ich",
            translation = "I",
            pronunciations = null
        ),
        Word(
            title = "",
            translation = "I",
            pronunciations = null
        ),
        Word(
            title = " ",
            translation = "I",
            pronunciations = null
        ),
        // Following 2 are empty
        Word(
            title = "spechieren",
            translation = "spec",
            pronunciations = null
        ),
        Word(
            title = "@$@!#$",
            translation = "@$@!#$",
            pronunciations = null
        ),
        Word(
            title = "Mediterrane Pflanzen halten das locker aus.\n Aber auch die brauchen ab und an mal Wasser",
            translation = "Mediterranean plants can easily handle this. But they also need water from time to time",
            pronunciations = null
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        floatingBubbleViewModel = FloatingBubbleViewModel(application)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun translateWord() = runTest {
        floatingBubbleViewModel.words.clear()
        germanWords.forEach {
            floatingBubbleViewModel.words.add(Word(title = it.title))
            floatingBubbleViewModel.currentWord = floatingBubbleViewModel.words.last()
            floatingBubbleViewModel.translateWord(word = it.title.refine()).join()
        }
        assertEquals(
            germanWords.map { it.translation },
            floatingBubbleViewModel.words.map { it.translation }
        )
    }

    @Test
    fun grabAudioFiles() = runTest {
        germanWords.map { it.title }.map {
            floatingBubbleViewModel.grabAudioFiles(
                searchTerm = it
            ).run {
                println(this?.attributes?.total)
            }
        }.let {
            println(it)
//            assertEquals(
//                germanWords.map { it.pronunciations.isNotEmpty() },
//                it
//            )
        }
    }

    @Test
    fun loadPronunciations() = runTest {
        germanWords.forEach {
            println(it.title)
            floatingBubbleViewModel.srcWord = TextFieldValue(it.title)
            floatingBubbleViewModel.loadPronunciations(
                searchTerm = it.title
            ).join()
        }
        println(floatingBubbleViewModel.words.toList())
    }
}