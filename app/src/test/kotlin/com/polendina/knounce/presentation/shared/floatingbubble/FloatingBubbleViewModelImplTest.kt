package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.polendina.knounce.data.database.DatabaseImpl
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.utils.refine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.time.Duration

//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
class FloatingBubbleViewModelImplTest {

    private lateinit var floatingBubbleViewModelImpl: FloatingBubbleViewModelImpl
    private lateinit var application: Application
    private var germanWords = listOf(
        Word(
            title = "nacht",
            translation = mutableStateMapOf("noun" to mutableStateListOf(Word.Translation("night", null))),
            pronunciations = mutableListOf("nacht" to "https://audio00.forvo.com/mp3/9218640/34/9218640_34_5784.mp3", "Verklärte Nacht" to "https://audio00.forvo.com/mp3/9170558/34/9170558_34_1767068_1.mp3", "Nacht-und-Nebel-Erlass" to "https://audio00.forvo.com/mp3/9218640/34/9218640_34_1906941.mp3", "Nacht-und-Nebel-Aktion" to "https://audio00.forvo.com/mp3/9223905/34/9223905_34_1906943_1.mp3", "Tag-Nacht-Rhythmus" to "https://audio00.forvo.com/mp3/9233940/34/9233940_34_3121228.mp3", "Frühjahrs-Tag-und-Nacht-Gleiche" to "https://audio00.forvo.com/mp3/9218640/34/9218640_34_3199878.mp3", "Tag-und-Nacht-Gleiche" to "https://audio00.forvo.com/mp3/9218640/34/9218640_34_3199881.mp3", "Tag-Nacht-Amplitude" to "https://audio00.forvo.com/mp3/9787104/34/9787104_34_5597595.mp3", "Über-Nacht-Lieferung" to "https://audio00.forvo.com/mp3/8973927/34/8973927_34_6069024.mp3"),
            loaded = false
        ),
        Word(
            title = "milch",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            loaded = false
        ),
        Word(
            title = "ich",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            loaded = false
        ),
        Word(
            title = "ich",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            loaded = false
        ),
        Word(
            title = "",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            loaded = false
        ),
        Word(
            title = " ",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            loaded = false
        ),
        // Following 2 are empty
        Word(
            title = "spechieren",
    //            translation = "spec",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            loaded = false
        ),
        Word(
            title = "@$@!#$",
    //            translation = "@$@!#$",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
            pronunciations = null,
            loaded = false
        ),
        Word(
            title = "Mediterrane Pflanzen halten das locker aus.\n Aber auch die brauchen ab und an mal Wasser",
            translation = mutableStateMapOf("" to mutableStateListOf(Word.Translation(null, null))),
    //            translation = "Mediterranean plants can easily handle this. But they also need water from time to time",
            pronunciations = null,
            loaded = false
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
//        application = Mockito.mock(Application::class.java)
        application = RuntimeEnvironment.getApplication()
        floatingBubbleViewModelImpl = FloatingBubbleViewModelImpl(
            application = application,
            database = DatabaseImpl(application = application)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun translateWordTest() = runTest {
        germanWords.take(1).forEach {
            Word(title = it.title.refine()).let {
                floatingBubbleViewModelImpl.words.add(it)
                floatingBubbleViewModelImpl.currentWord = it
            }
            floatingBubbleViewModelImpl.translateWord().join()
        }
        advanceUntilIdle()
        assert(
            germanWords.take(1).map { it.translation?.values }.map { it?.toList()?.map { it.toList() } } ==
            floatingBubbleViewModelImpl.words.map { it.translation?.values?.toList()?.map { it.toList() } }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadPronunciations() = runTest(timeout = Duration.INFINITE) {
        germanWords.take(1).forEachIndexed { index, word ->
            floatingBubbleViewModelImpl.searchWord(word.title)
            floatingBubbleViewModelImpl.loadPronunciations()
        }
        advanceUntilIdle()
        assert(
            floatingBubbleViewModelImpl.words.map { it.pronunciations?.take(3)?.toList() } ==
            germanWords.take(1).map { it.pronunciations?.take(3)?.toList() }
        )
    }

    @Test
    fun searchWordTest() = runTest {
        germanWords.map { it.title }.forEach {
            floatingBubbleViewModelImpl.searchWord(it)
        }
        assert(
            germanWords.map { it.title }.filter { it.isNotBlank() }.distinct() ==
            floatingBubbleViewModelImpl.words.toList().map { it.title }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadWordsFromDbTest() = runTest {
        germanWords.forEach {
            floatingBubbleViewModelImpl.searchWord(it.title)
            println(floatingBubbleViewModelImpl.currentWord.title)
            floatingBubbleViewModelImpl.insertWordToDb(floatingBubbleViewModelImpl.currentWord)
        }
        advanceUntilIdle()
        println(floatingBubbleViewModelImpl.loadWordsFromDb().map { it.title })
    }

}