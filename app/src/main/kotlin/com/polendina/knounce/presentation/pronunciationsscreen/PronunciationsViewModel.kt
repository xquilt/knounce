package com.polendina.knounce.presentation.pronunciationsscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.polendina.knounce.data.repository.pronunciation.ForvoPronunciation
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages

class PronunciationsViewModel(
//    private val pronunciationRepository: ForvoPronunciation // its methods were getting skipped over when called down below
): ViewModel() {

    // Expose screen UI state
    private var _query = mutableStateOf("")
    val query = _query
    private val _languages = mutableStateListOf<Pronunciations.Datum>()
//    private val _languages = mutableStateListOf<String>("Arabic", "English", "French", "Spanish", "Interlingua")
    val languages = _languages
    val highlightedLanguages = mutableStateListOf<LanguageSelected>()

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
        _languages.clear()
        _languages.addAll(newList.data)
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
                highlightedLanguages.addAll(_languages.map { LanguageSelected(it.language, false) })
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