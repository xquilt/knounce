package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Application
import android.app.Service
import android.content.ClipboardManager
import android.content.Intent
import android.os.IBinder
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
    var srcWord by mutableStateOf(TextFieldValue(text = ""))
    var srcWordDisplay by mutableStateOf("")
    var targetWordDisplay by mutableStateOf("")
    val ioScope = CoroutineScope(Dispatchers.IO)
    var expanded by mutableStateOf(true)
    // FIXME: Failed attempted to access the clipboard from within the View model
//    val clipboardManager = application.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
//    private val clipboardContent = clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
    val loadedPronunciations = mutableStateMapOf<String, List<Pair<String, String>>>()
//    val loadedPronunciations = mutableMapOf("" to listOf(("einem" to ""), ("seit einema monat" to ""), ("einem" to ""), ("seit einem monat" to ""), ("seit einem monat" to "")))

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
     * @param shuffle Determine whether to play a the single first pronunciation, or shuffle randomly through available pronunciations.
     */
    suspend fun grabAudioFile(
        searchTerm: String,
        shuffle: Boolean
    ): String {
        var url = ""
        loadedPronunciations.get(searchTerm)?.let {
            url = if (shuffle) it.random().second else it.first().second
        }
        retrofitInstance.wordPronunciations(
            word = searchTerm,
            interfaceLanguageCode = UserLanguages.ENGLISH.code,
            languageCode = FORVO_LANGUAGE.GERMAN.code
        ).execute().let {
            try {
                it.body()?.data?.first()?.items?.map {item ->
                    item.original to
                    Gson().fromJson(
                        item.standard_pronunciation,
                        Item.StandardPronunciation::class.java
                    ).realmp3
                }?.let {
                    loadedPronunciations.put(searchTerm, it)
                    url = if (shuffle) it.random().second else it.first().second
                }
            } catch (e: NoSuchElementException) {}
        }
        return(url)
    }

    /**
     * Play remote pronunciation audio.
     *
     * @param searchTerm: String,
     * @param shuffle: Boolean
    */
    fun playAudio(
        searchTerm: String,
        shuffle: Boolean
    )  {
        ioScope.launch {
            PronunciationPlayer.playRemoteAudio(grabAudioFile(
                searchTerm = searchTerm,
                shuffle = shuffle
            ))
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