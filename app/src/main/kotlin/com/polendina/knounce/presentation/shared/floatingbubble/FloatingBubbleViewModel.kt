package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.data.repository.pronunciation.ForvoPronunciation
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.UserLanguages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.bush.translator.Language
import me.bush.translator.Translator
import trancore.corelib.pronunciation.retrofitInstance

class FloatingBubbleViewModel : ViewModel() {
    var srcWord by mutableStateOf("") // TODO: I should load this value from string resources, but can't use stringResource() outside of a 'composable'
    var targetWord by mutableStateOf("")
    val ioScope = CoroutineScope(Dispatchers.IO)
    var expanded by mutableStateOf(true)
    private val loadedPronunciations = mutableMapOf<String, String>()

    fun translateWord() {
        ioScope.launch {
            Translator().translate(
                text = srcWord,
                source = Language.AUTO,
                target = Language.ENGLISH
            ).apply {
                targetWord = translatedText
            }
        }
    }

    fun just(
        searchTerm: String
    ) {
        loadedPronunciations.get(searchTerm)?.let {
            PronunciationPlayer.playRemoteAudio(it)
            return
        }
        ioScope.launch {
            retrofitInstance.wordPronunciations(
                word = searchTerm,
                interfaceLanguageCode = UserLanguages.ENGLISH.code,
                languageCode = FORVO_LANGUAGE.GERMAN.code
            ).execute().let {
                try {
                    Gson().fromJson(it.body()?.data?.first()?.items?.first()?.standard_pronunciation, Item.StandardPronunciation::class.java).let {
                        loadedPronunciations.put(searchTerm, it.realmp3)
                        PronunciationPlayer.playRemoteAudio(it.realmp3)
                    }
                } catch (e: NoSuchElementException) {}
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