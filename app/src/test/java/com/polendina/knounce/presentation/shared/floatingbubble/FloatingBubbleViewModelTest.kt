package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.polendina.knounce.data.database.Word
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
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            false
        ),
        Word(
            title = "milch",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            false
        ),
        Word(
            title = "ich",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            false
        ),
        Word(
            title = "ich",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            false
        ),
        Word(
            title = "",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            false
        ),
        Word(
            title = " ",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            false
        ),
        // Following 2 are empty
        Word(
            title = "spechieren",
    //            translation = "spec",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            false
        ),
        Word(
            title = "@$@!#$",
    //            translation = "@$@!#$",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            false
        ),
        Word(
            title = "Mediterrane Pflanzen halten das locker aus.\n Aber auch die brauchen ab und an mal Wasser",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
    //            translation = "Mediterranean plants can easily handle this. But they also need water from time to time",
            pronunciations = null,
            false
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
//        floatingBubbleViewModel.words.clear()
//        germanWords.subList(0, 1).forEach {
//            floatingBubbleViewModel.words.add(Word(title = it.title.refine()))
//            floatingBubbleViewModel.currentWord = floatingBubbleViewModel.words.last()
//            floatingBubbleViewModel.translateWord().join()
//        }
        floatingBubbleViewModel.currentWord = Word("danke", null, null, false)
        floatingBubbleViewModel.translateWord().join()
        println(floatingBubbleViewModel.currentWord)
//        assertEquals(
//            germanWords.map { it.translation },
//            val j = floatingBubbleViewModel.words.map { it.translation }
//            println(j)
//        )
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
        germanWords.subList(0, 1).forEach {
            println(it.title)
//            floatingBubbleViewModel.srcWord = TextFieldValue(it.title)
            floatingBubbleViewModel.currentWord = com.polendina.knounce.domain.model.WordDb(it.title)
            floatingBubbleViewModel.loadPronunciations().join()
        }
        println(floatingBubbleViewModel.currentWord.pronunciations?.map { it.first })
//        println(floatingBubbleViewModel.words.toList())
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