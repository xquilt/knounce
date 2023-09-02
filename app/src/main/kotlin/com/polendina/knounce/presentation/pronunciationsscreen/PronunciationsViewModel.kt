package com.polendina.knounce.presentation.pronunciationsscreen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.polendina.knounce.NetworkHandler
import com.polendina.knounce.data.repository.pronunciation.ForvoPronunciation
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages

class PronunciationsViewModel(
    application: Application = Application()
//    private val pronunciationRepository: ForvoPronunciation // its methods were getting skipped over when called down below
//): ViewModel() {
): AndroidViewModel(application) {

    // Expose screen UI state
    private var _query = mutableStateOf("")
    val query = _query
    val languages = mutableStateListOf<Pronunciations.Datum>()
//    private val _languages = mutableStateListOf<String>("Arabic", "English", "French", "Spanish", "Interlingua")
    val highlightedLanguages = mutableStateListOf<LanguageSelected>()
    var networkConnected = false
        get() = NetworkHandler(application = getApplication<Application>()).isNetworkAvailable()
        private set

    var showImage by mutableStateOf(true)

    // Handle business logic
    fun updateQuery(newQuery: String) {
        _query.value = newQuery
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
    private val pronunciationRepository = ForvoPronunciation()

    /**
     * Search for pronunciations of a single term.
     *
     * @param searchTerm The search term to be used.
    */
    fun wordPronunciationsAll(searchTerm: String) {

        pronunciationRepository.wordPronunciationsAll(
            word = searchTerm,
            interfaceLanguageCode = UserLanguages.ENGLISH.code
        ) {
            it?.apply {
                updateLanguagesList(it)
                highlightedLanguages.clear()
                highlightedLanguages.addAll(this@PronunciationsViewModel.languages.map { LanguageSelected(it.language, false) })
            }
        }
    }

    fun filterPronunciations(pronunciationLanguage: LanguageSelected) {
        if (highlightedLanguages.size == highlightedLanguages.filter { it.selected }.size) highlightedLanguages.forEach { it.selected = false }
        highlightedLanguages.indexOf(pronunciationLanguage).run {
            highlightedLanguages[this] = pronunciationLanguage.copy(selected = !highlightedLanguages[this].selected)
        }
    }

}
data class LanguageSelected(
    val name: String,
    var selected: Boolean
)