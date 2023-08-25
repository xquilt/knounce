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
    private val _pronunciationsList = mutableStateListOf<Pronunciations.Datum.Item>()
    val pronunciationsList = _pronunciationsList
    private var _query = mutableStateOf("")
    val query = _query
    private val _languages = mutableStateListOf<String>()
//    private val _languages = mutableStateListOf<String>("Arabic", "English", "French", "Spanish", "Interlingua")
    val languages = _languages
    private val _highlighted_languages = mutableStateListOf<String>()
    val highlighted_languages = _highlighted_languages

    // Handle business logic
    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }
    private fun updatePronunciationsList(newList: Pronunciations) {
        _pronunciationsList.clear()
        newList.data.forEach {
            _pronunciationsList.addAll(it.items)
        }
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
                updatePronunciationsList(it)
                _languages.clear()
                it.data.forEach {
                    _languages.add(it.language)
                }
            }
        }
    }

}