package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.UserLanguages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.bush.translator.Language
import me.bush.translator.Translator
import trancore.corelib.pronunciation.retrofitInstance

class FloatingBubbleViewModel(
    application: Application = Application()
) : AndroidViewModel(application) {
    var srcWord by mutableStateOf(TextFieldValue(""))
    var srcWordDisplay by mutableStateOf("")
    var targetWordDisplay by mutableStateOf("")
//    var srcWord by mutableStateOf(TextFieldValue(text = loremIpsum))
//    var srcWordDisplay by mutableStateOf(loremIpsum)
//    var targetWordDisplay by mutableStateOf(loremIpsum)
    val ioScope = CoroutineScope(Dispatchers.IO)
    var expanded by mutableStateOf(true)
    // FIXME: Failed attempted to access the clipboard from within the View model
//    val clipboardManager = application.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
//    private val clipboardContent = clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
    val loadedPronunciations = mutableStateMapOf<String, List<Pair<String, String>>>()
//    val loadedPronunciations = mutableStateMapOf("" to listOf(("einem" to ""), ("seit einema monat" to ""), ("einem" to ""), ("seit einem monat" to ""), ("seit einem monat" to "")))

    /**
     * Translate the current value of the text field.
     *
     */
    fun translateWord() {
        ioScope.launch {
            Translator().translate(
                text = srcWordDisplay,
                source = Language.AUTO,
                target = Language.ENGLISH
            ).apply {
                targetWordDisplay = translatedText
            }
        }
    }

    /**
     * Play remote pronunciation audio.
     * Grab the appropriate direct audio file URL.
     *
     * @param searchTerm The word to be pronounced.
     */
    suspend fun grabAudioFiles(
        searchTerm: String
    ): List<Pair<String, String>> {
        retrofitInstance.wordPronunciations(
            word = searchTerm,
            interfaceLanguageCode = UserLanguages.ENGLISH.code,
            languageCode = FORVO_LANGUAGE.GERMAN.code
        ).execute().let {
            try {
                return it.body()?.data?.first()?.items?.map {item ->
                    item.original to
                    Gson().fromJson(
                        item.standard_pronunciation,
                        Item.StandardPronunciation::class.java
                    ).realmp3
                } ?: emptyList()
            } catch (e: NoSuchElementException) {}
        }
        return (emptyList())
    }

    fun loadPronunciations(
        searchTerm: String
    ) {
        if (!loadedPronunciations.contains(searchTerm)) {
            ioScope.launch {
                grabAudioFiles(searchTerm = searchTerm).let {
                    loadedPronunciations.put(searchTerm, it)
                }
            }
        }
    }

    /**
     * Play remote pronunciation audio.
     *
     * @param searchTerm: The word to play its pronunciation.
    */
    fun playAudio(
        searchTerm: String
    ) {
        ioScope.launch {
            loadedPronunciations.get(searchTerm)?.firstOrNull()?.second?.let {audioUrl ->
                PronunciationPlayer.playRemoteAudio(audioUrl)
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

