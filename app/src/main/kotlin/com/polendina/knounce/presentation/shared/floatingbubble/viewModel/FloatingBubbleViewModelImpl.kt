package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.domain.repository.PronunciationsRepository
import com.polendina.knounce.presentation.shared.floatingbubble.viewModel.FloatingBubbleViewModel
import com.polendina.knounce.utils.refine
import com.polendina.knounce.utils.swap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.bush.translator.Language
import me.bush.translator.Translator
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FloatingBubbleViewModelImpl(
    private val application: Application,
    private val pronunciationsRepository: PronunciationsRepository
) : AndroidViewModel(application), FloatingBubbleViewModel {
    override var srcWord by mutableStateOf(TextFieldValue(""))
    override var targetWordDisplay by mutableStateOf("")

    override var expanded by mutableStateOf(true)

    // FIXME: Failed attempted to access the clipboard from within the View model
//    val clipboardManager = application.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
//    private val clipboardContent = clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
    // TODO: I'm not sure if this is the right way to go about saving the words (transforming them from LiveData to MutableState)
//    override val words: LiveData<List<Word>>
    override val words: MutableList<Word> = pronunciationsRepository.words.value?.toMutableStateList() ?: mutableStateListOf()
    override var currentWord by mutableStateOf(Word())
    override var pageIndex by mutableIntStateOf(words.size)

    /**
     * Invert the loaded state of the current word.
     *
     */
    override fun invertLoaded() {
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
    override fun searchWord(word: String) {
        // Instantly add a Word synchronously, to avoid unnecessary null checks and race conditions with translation & pronunciations network requests.
        if (word.isBlank()) return
        val insertIndex = if (words.size == 0) 0 else pageIndex + 1
        words.find { it.title == word }.let { foundWord ->
            if (foundWord == null) {
                words.add(index = insertIndex, Word(title = word))
                pageIndex = insertIndex
                currentWord = words[insertIndex]
                expanded = true
                try {
                    viewModelScope.launch {
                        translateWord(word = currentWord.title)
                        withContext(Dispatchers.IO) {
                            loadPronunciations(word = currentWord.title)
                        }
                    }
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace(); println(e.cause)
                } catch (_: IOException) {}
            } else {
                // Maintain whichever the current page/word as the previous page when navigating between various indices.
                if (pageIndex != words.indexOf(foundWord)) words.swap(pageIndex + 1, words.indexOf(foundWord))
                currentWord = foundWord
                pageIndex = words.indexOf(foundWord)
                expanded = true
            }
        }
    }

    /**
     * Translate the current value of the text field, then modify the ViewModel internal states.
     *
     */
    // TODO: Add the ability to display auto-corrections for malformed words inputted by the user.
    override suspend fun translateWord(word: String) {
        Translator().translate(
            text = word,
            source = Language.GERMAN,
            target = Language.ENGLISH
        ).let {
             currentWord.copy(
                translation = mutableStateMapOf(
                    currentWord.title to
                    mutableStateListOf(Word.Translation(
                        explanation = it.translatedText,
                        examples = null
                    ))
                )
            ).let {
                 currentWord = it
                 words[pageIndex] = it
             }
        }
    }

    /**
     * Play remote pronunciation audio.
     * Grab the appropriate direct audio file URL.
     *
     * @param searchTerm The word to be pronounced.
     * @return Pronunciations object of the searched word.
     */
    override suspend fun grabAudioFiles(searchTerm: String): Pronunciations? = suspendCoroutine { continuation ->
        pronunciationsRepository.wordPronunciations(
            word = searchTerm.refine(),
            interfaceLanguageCode = UserLanguages.ENGLISH.code,
            languageCode = FORVO_LANGUAGE.GERMAN.code
        ) { pronunciations ->
            continuation.resume(value = pronunciations)
        }
    }

    /**
     * If the word pronunciations aren't already loaded, then simply retrieve and append them.
     *
     * @param searchTerm The desired word to be pronounced.
     */
    override suspend fun loadPronunciations(word: String) {
        grabAudioFiles(searchTerm = word).let { pronunciations ->
            println(pronunciations)
            pronunciations?.data?.forEach {
                currentWord.copy(
                    pronunciations = it.items.map { item ->
                        item.original to
                        Gson().fromJson(
                            item.standard_pronunciation,
                            Item.StandardPronunciation::class.java
                        ).realmp3
                    }.toMutableStateList()
                ).let { word ->
                    currentWord = word
                    words[pageIndex] = word
                }
            }
            pronunciationsRepository.loadWords()
        }
    }

    /**
     * Play remote pronunciation audio.
     *
     * @param searchTerm The word to play its pronunciation.
    */
    override fun playAudio(searchTerm: String) = viewModelScope.launch {
        currentWord.pronunciations
            ?.find { it.first == searchTerm }
            ?.let {
                PronunciationPlayer.playRemoteAudio(it.second)
            }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    private suspend fun refreshWords() = runCatching {
        pronunciationsRepository.loadWords()
    }

    override fun insertWord(word: Word) = viewModelScope.launch {
        pronunciationsRepository.insertWord(word)
    }

    override fun removeWord(wordTitle: String) = viewModelScope.launch {
        pronunciationsRepository.removeWord(wordTitle)
    }

    init {
        viewModelScope.launch {
//            withContext(Dispatchers.IO) { }
            refreshWords()
                .onFailure {
                    // TODO: Show any form of an indication to the user!
                    if (it is IOException) { // TODO: The previous implementation relied on IOException
                        Log.e("NetworkError", it.message.toString())
                    }
                }
                .onSuccess {
                    Log.d("Success!", "Hello world!")
                }
            Log.d("Value", pronunciationsRepository.words.value.toString())
            delay(5000)
            Log.d("Value", pronunciationsRepository.words.value.toString())
            delay(5000)
            withContext(Dispatchers.IO) {
                pronunciationsRepository.loadWords()
            }
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