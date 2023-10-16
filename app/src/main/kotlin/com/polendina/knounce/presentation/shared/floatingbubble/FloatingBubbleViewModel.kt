package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.data.database.WordDatabase
import com.polendina.knounce.data.database.WordDb
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.domain.model.Word
import com.polendina.knounce.utils.refine
import com.polendina.knounce.utils.swap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.bush.translator.Language
import me.bush.translator.Translator
import retrofit2.awaitResponse
import trancore.corelib.pronunciation.retrofitInstance
import java.io.IOException
import java.net.SocketTimeoutException

class FloatingBubbleViewModel(
    private val application: Application,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
//    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
//    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main
) : AndroidViewModel(application) {
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
    var currentWord by mutableStateOf(Word())
//    val words = mutableStateListOf(Word(title = "Einem", pronunciations = listOf(("einem" to ""), ("seit einema monat" to ""), ("einem" to ""), ("seit einem monat" to ""), ("seit einem monat" to ""))), Word(title = "Nacht"), Word("hallo"), Word("schlieben"), Word("eingeben"), Word("ritter"), Word("der"), Word("Milch"))
    var pageIndex by mutableIntStateOf(words.size)

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
                words.add(index = insertIndex, Word(title = word))
                pageIndex = insertIndex
                currentWord = words[insertIndex]
                expanded = true
                try {
                    translateWord(word = currentWord.title)
                    loadPronunciations(searchTerm = currentWord.title)
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
    fun translateWord(word: String) = viewModelScope.launch {
        // FIXME: Under certain conditions it raises an exception. When attempting to parse a lengthy paragraph with wordvomit within. IllegalArgumentException
        try {
            currentWord.translation = Translator().translate(
                text = word,
                source = Language.GERMAN,
                target = Language.ENGLISH
            ).translatedText
        } catch (e: IllegalArgumentException) { e.printStackTrace(); println(e.cause) }
    }

    /**
     * Play remote pronunciation audio.
     * Grab the appropriate direct audio file URL.
     *
     * @param searchTerm The word to be pronounced.
     * @return Pronunciations object of the searched word.
     */
    suspend fun grabAudioFiles(searchTerm: String) = retrofitInstance.wordPronunciations(
        word = searchTerm.refine(),
        interfaceLanguageCode = UserLanguages.ENGLISH.code,
        languageCode = FORVO_LANGUAGE.GERMAN.code
    ).awaitResponse().body()

    /**
     * If the word pronunciations aren't already loaded, then simply retrieve and append them.
     *
     * @param searchTerm The desired word to be pronounced.
     */
    fun loadPronunciations(searchTerm: String) = viewModelScope.launch {
        currentWord.pronunciations = grabAudioFiles(searchTerm = searchTerm)
    }

    /**
     * Play remote pronunciation audio.
     *
     * @param searchTerm: The word to play its pronunciation.
    */
    fun playAudio(searchTerm: String) = currentWord
        .pronunciations
        ?.parseAudios()
        ?.find { it.first == searchTerm }
        ?.let {
            viewModelScope.launch {
                PronunciationPlayer.playRemoteAudio(it.second)
            }
        }

    private val database by lazy { WordDatabase.getDatabase(application) }
    private val wordDao = database.wordDao

    /**
     * load words from the local Room database.
     *
     * @return Return a List of Words.
     */
    suspend fun loadWordsFromDb(): List<Word> = wordDao.getWords().map {
        Word(
            title = it.title,
            translation = it.translation,
            pronunciations = it.pronunciations,
            loaded = it.loaded
        )
    }

    fun insertWordToDb(word: Word) = viewModelScope.launch {
        wordDao.insertWord(WordDb(
            title = word.title,
            translation = word.translation,
            pronunciations = word.pronunciations,
            loaded = true
        ))
    }

    /**
     * Remove a word from the database, but not from the current viewModel list.
     *
     * @param word The Word to be delete. Obtained frht moe current viewmodel.
     */
    fun removeWordFromDb(word: Word) = viewModelScope.launch {
        wordDao.deleteWord(word.title)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    init {
        viewModelScope.launch {
            words.addAll(loadWordsFromDb())
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

/**
 *
 * @return A List of string pairs representing the
 */
fun Pronunciations.parseAudios() = this.data.firstOrNull()?.items?.map { item ->
    item.original to
    Gson().fromJson(
        item.standard_pronunciation,
        Item.StandardPronunciation::class.java
    ).realmp3
} ?: emptyList()