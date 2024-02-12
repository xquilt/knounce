package com.polendina.knounce.presentation.pronunciationsscreen

import NetworkHandler
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.data.database.WordDatabase
import com.polendina.knounce.data.network.ForvoService
import com.polendina.knounce.data.repository.KnounceRepositoryImpl
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.domain.repository.KnounceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PronunciationsViewModel(
    private val application: Application = Application(),
    private val knounceRepository: KnounceRepository // its methods were getting skipped over when called down below
): AndroidViewModel(application) {

    // Expose screen UI state
    var query = mutableStateOf("")
        private set
    val languages = mutableStateListOf<Pronunciations.Datum>()
    val highlightedLanguages = mutableStateListOf<LanguageSelected>()
    var networkConnected = false
        get() = NetworkHandler(context = application).isNetworkAvailable()
        private set

    var showImage by mutableStateOf(false)

    fun updateQuery(newQuery: String) {
        this.query.value = newQuery
    }

    /**
     *
     *
     * @param newList
     */
    private fun updateLanguagesList(newList: Pronunciations) {
        this.languages.clear()
        this.languages.addAll(newList.data)
    }

    /**
     * Search for pronunciations of a single term.
     *
     * @param searchTerm The search term to be used.
    */
    fun wordPronunciations(searchTerm: String) = viewModelScope.launch(Dispatchers.IO) {
        knounceRepository
            .wordPronunciations(
                word = searchTerm,
                languageCode = UserLanguages.ENGLISH.code,
                interfaceLanguageCode = UserLanguages.ENGLISH.code
            )?.let { // TODO:  I should make a distinctive UI widgets for different cases of data being correctly loaded/actually empty
                updateLanguagesList(it)
                highlightedLanguages.clear()
                highlightedLanguages.addAll(languages.map {
                    LanguageSelected(it.language, false)
                })
            }
    }

    fun filterPronunciations(
        pronunciationLanguage: LanguageSelected
    ) {
        if (highlightedLanguages.size == highlightedLanguages.filter { it.selected }.size) highlightedLanguages.forEach { it.selected = false }
        highlightedLanguages.indexOf(pronunciationLanguage).run {
            highlightedLanguages[this] = pronunciationLanguage.copy(selected = !highlightedLanguages[this].selected)
        }
    }

    fun playRemoteAudio(pronunciation: Item): Boolean {
        Gson().fromJson(pronunciation.standard_pronunciation, Item.StandardPronunciation::class.java).realmp3.run {
//            if (NetworkHandler(context).isNetworkAvailable()) {
            if (networkConnected) {
                PronunciationPlayer.playRemoteAudio(this)
                return (true)
            } else {
                return (false)
            }
        }
    }

    // TODO: This should be read from the user configurations/settings.
    var preventScreenFromDimming by mutableStateOf(true)

}
data class LanguageSelected(
    val name: String,
    var selected: Boolean
)

fun pronunciationsRepositoryImpl(application: Application) = KnounceRepositoryImpl(
    forvoService = ForvoService,
    wordDatabase = WordDatabase.getDatabase(context = application.applicationContext)
)