package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import com.polendina.knounce.domain.model.Word
import com.polendina.knounce.utils.refine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
class FloatingBubbleViewModelTest {

    private lateinit var floatingBubbleViewModel: FloatingBubbleViewModel
    private lateinit var application: Application
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
    @Before
    fun setup() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
//        application = Mockito.mock(Application::class.java)
        application = RuntimeEnvironment.getApplication()
        floatingBubbleViewModel = FloatingBubbleViewModel(application, testDispatcher)
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

    @Test
    fun searchWordTest() = runTest {
        germanWords.map { it.title }.forEach {
            floatingBubbleViewModel.searchWord(it)
        }
        assertEquals(
            germanWords.map { it.title }.filter { it.isNotBlank() }.distinct(),
            floatingBubbleViewModel.words.toList().map { it.title }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadWordsFromDbTest() = runTest {
        germanWords.forEach {
            floatingBubbleViewModel.searchWord(it.title)
            println(floatingBubbleViewModel.currentWord.title)
            floatingBubbleViewModel.insertWordToDb(floatingBubbleViewModel.currentWord)
        }
        advanceUntilIdle()
        println(floatingBubbleViewModel.loadWordsFromDb().map { it.title })
    }

}