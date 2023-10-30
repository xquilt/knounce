package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.data.database.Database
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.utils.refine
import com.polendina.knounce.utils.swap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.bush.translator.Language
import me.bush.translator.Translator
import retrofit2.awaitResponse
import trancore.corelib.pronunciation.retrofit
import java.io.IOException
import java.net.SocketTimeoutException

class FloatingBubbleViewModel(
    private val application: Application,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
    val database: Database
//    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
//    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main
) : AndroidViewModel(application), Database {
    var srcWord by mutableStateOf(TextFieldValue(""))
    var targetWordDisplay by mutableStateOf("")
    val viewModelScope = CoroutineScope(coroutineDispatcher)

//    var srcWord by mutableStateOf(TextFieldValue(text = loremIpsum))
//    var srcWordDisplay by mutableStateOf(loremIpsum)
//    var targetWordDisplay by mutableStateOf(loremIpsum)
    var expanded by mutableStateOf(true)

    // FIXME: Failed attempted to access the clipboard from within the View model
//    val clipboardManager = application.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
//    private val clipboardContent = clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
    val words = mutableStateListOf<Word>()
    var currentWord by mutableStateOf(Word(title = "", translation = null, pronunciations = null, loaded = false))
    var pageIndex by mutableIntStateOf(words.size)

    /**
     * Invert the loaded state of the current word.
     *
     */
    fun invertLoaded() {
        currentWord = currentWord.copy(
            loaded = !currentWord.loaded
        )
    }

    /**
     * Callback function that's invoked when searching a word, in order to load its pronunciations
     * and translations.
     *
     * @param word The word to be searched for.
     */
    // FIXME: I guess it should be called after expanded, because it's somewhat blocking of some kind. IDK
    fun searchWord(word: String) {
        // Instantly add a Word synchronously, to avoid unnecessary null checks and race conditions with translation & pronunciations network requests.
        if (word.isBlank()) return
        val insertIndex = if (words.size == 0) 0 else pageIndex + 1
        words.find { it.title == word }.let {
            if (it == null) {
                words.add(index = insertIndex, Word(title = word, translation = null, pronunciations = null, loaded = false))
                pageIndex = insertIndex
                currentWord = words[insertIndex]
                expanded = true
                try {
                    translateWord()
                    loadPronunciations()
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace(); println(e.cause)
                } catch (_: IOException) {}
            } else {
                // Maintain whichever the current page/word as the previous page when navigating between various indices.
                if (pageIndex != words.indexOf(it)) words.swap(pageIndex + 1, words.indexOf(it))
                currentWord = it
                pageIndex = words.indexOf(it)
                expanded = true
            }
        }
    }

    /**
     * Translate the current value of the text field, then modify the ViewModel internal states.
     *
     */
    // TODO: Add the ability to display auto-corrections for malformed words inputted by the user.
    fun translateWord() = viewModelScope.launch {
        Translator().translate(
            text = currentWord.title,
            source = Language.GERMAN,
            target = Language.ENGLISH
        ).let {
            currentWord = currentWord.copy(
                translation = mutableStateMapOf(
                    currentWord.title to
                    mutableStateListOf(Word.Translation(
                        explanation = it.translatedText,
                        examples = null
                    ))
                )
            )
        }
    }

    /**
     * Play remote pronunciation audio.
     * Grab the appropriate direct audio file URL.
     *
     * @param searchTerm The word to be pronounced.
     * @return Pronunciations object of the searched word.
     */
    suspend fun grabAudioFiles(searchTerm: String) = retrofit.wordPronunciations(
        word = searchTerm.refine(),
        interfaceLanguageCode = UserLanguages.ENGLISH.code,
        languageCode = FORVO_LANGUAGE.GERMAN.code
    ).awaitResponse().body()

    /**
     * If the word pronunciations aren't already loaded, then simply retrieve and append them.
     *
     * @param searchTerm The desired word to be pronounced.
     */
    fun loadPronunciations() = viewModelScope.launch {
        grabAudioFiles(searchTerm = currentWord.title).let {
            it?.data?.forEach {
                currentWord = currentWord.copy(
                    pronunciations = it.items.map {
                        it.original to
                        Gson().fromJson(
                            it.standard_pronunciation,
                            Item.StandardPronunciation::class.java
                        ).realmp3
                    }.toMutableStateList()
                )
            }
        }
    }

    /**
     * Play remote pronunciation audio.
     *
     * @param searchTerm The word to play its pronunciation.
    */
    fun playAudio(searchTerm: String) = currentWord
        .pronunciations
        ?.find { it.first == searchTerm }
        ?.let {
            viewModelScope.launch {
                PronunciationPlayer.playRemoteAudio(it.second)
            }
        }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    override fun insertWordToDb(word: Word): Job = database.insertWordToDb(word)

    override suspend fun loadWordsFromDb(): List<Word> = database.loadWordsFromDb()

    override fun removeWordFromDb(wordTitle: String): Job = database.removeWordFromDb(wordTitle)

    init {
        viewModelScope.launch {
            words.addAll(database.loadWordsFromDb())
        }
    }

}

enum class FORVO_LANGUAGE(
    val title: String,
    val code: String
) {
    FRENCH("French", "fr"),
    GERMAN("German", "de")
}
const val LOREM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."