package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.UserLanguages
import kotlinx.coroutines.launch
import me.bush.translator.Language
import me.bush.translator.Translator
import retrofit2.awaitResponse
import trancore.corelib.pronunciation.retrofitInstance

class Word(
    title: String? = null,
    translation: String? = null,
    pronunciations: List<Pair<String, String>> = listOf()
) {
    var title by mutableStateOf(title)
    var translation by mutableStateOf(translation)
    var pronunciations: SnapshotStateList<Pair<String, String>> = pronunciations.toMutableStateList()
}

class FloatingBubbleViewModel(
    private val application: Application = Application(),
//    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
//    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main
) : AndroidViewModel(application) {
    var srcWord by mutableStateOf(TextFieldValue(""))
    var srcWordDisplay by mutableStateOf("")
    var targetWordDisplay by mutableStateOf("")

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
    var pageIndex by mutableIntStateOf(0)

    /**
     * Callback function called when the search button is pressed at the search bubble.
     */
    fun goToArrowCallback() {
        addWord()
        pageIndex = words.lastIndex
        currentWord = words[pageIndex]
        expanded = true
        srcWordDisplay = srcWord.text
        translateWord()
        loadPronunciations(
            searchTerm = srcWord.text
        )
    }

    /**
     * Instantly add a Word synchronously, to avoid unnecessary null checks and race conditions
     * with translation & pronunciations network requests.
     *
     */
    fun addWord() {
        if (words.find { it.title == srcWord.text } == null) words.add(Word(title = srcWord.text))
    }

    /**
     * Translate the current value of the text field, then modify the ViewModel internal states.
     *
     */
    fun translateWord() = viewModelScope.launch {
        currentWord.translation = Translator().translate(
            text = srcWordDisplay,
            source = Language.GERMAN,
            target = Language.ENGLISH
        ).translatedText
    }

    /**
     * Play remote pronunciation audio.
     * Grab the appropriate direct audio file URL.
     *
     * @param searchTerm The word to be pronounced.
     */
    suspend fun grabAudioFiles(
        searchTerm: String
    ): List<Pair<String, String>> = retrofitInstance.wordPronunciations(
            word = searchTerm,
            interfaceLanguageCode = UserLanguages.ENGLISH.code,
            languageCode = FORVO_LANGUAGE.GERMAN.code
        ).awaitResponse().run {
            body()?.data?.firstOrNull()?.items?.map {item ->
                item.original to
                Gson().fromJson(
                    item.standard_pronunciation,
                    Item.StandardPronunciation::class.java
                ).realmp3
            } ?: emptyList()
        }

    /**
     * If the word pronunciations aren't already loaded, then simply retrieve and append them.
     *
     * @param searchTerm The desired word to be pronounced.
     */
    fun loadPronunciations(searchTerm: String) = viewModelScope.launch {
        currentWord.pronunciations.addAll(grabAudioFiles(searchTerm = searchTerm).toMutableStateList())
    }

    /**
     * Play remote pronunciation audio.
     *
     * @param searchTerm: The word to play its pronunciation.
    */
    fun playAudio(
        searchTerm: String
    ) {
        words
            .find { it.title == srcWordDisplay }
            ?.pronunciations
            ?.find { it.first == searchTerm }
            ?.let {
                viewModelScope.launch {
                    PronunciationPlayer.playRemoteAudio(it.second)
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
const val LOREM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."