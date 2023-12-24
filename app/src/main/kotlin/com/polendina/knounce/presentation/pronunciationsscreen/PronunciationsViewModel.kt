package com.polendina.knounce.presentation.pronunciationsscreen

import NetworkHandler
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.data.database.WordDatabase
import com.polendina.knounce.data.network.ForvoService
import com.polendina.knounce.data.repository.PronunciationsRepositoryImpl
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.model.UserLanguages
import com.polendina.knounce.domain.repository.PronunciationsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import trancore.corelib.pronunciation.P

class PronunciationsViewModel(
    private val application: Application = Application(),
    private val pronunciationRepository: PronunciationsRepository // its methods were getting skipped over when called down below
): AndroidViewModel(application) {

    // Expose screen UI state
    var query = mutableStateOf("")
        private set
    val languages = mutableStateListOf<Pronunciations.Datum>()
    private val _languages = mutableStateListOf<String>("Arabic", "English", "French", "Spanish", "Interlingua")
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
    fun wordPronunciationsAll(
        searchTerm: String
    ) {
        pronunciationRepository
        .wordPronunciationsAll(
            word = searchTerm,
            interfaceLanguageCode = UserLanguages.ENGLISH.code
        ) {
            it?.apply {
                updateLanguagesList(it)
                highlightedLanguages.clear()
                highlightedLanguages.addAll(languages.map { LanguageSelected(it.language, false) })
            }
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

fun pronunciationsRepositoryImpl(application: Application) = PronunciationsRepositoryImpl(
    forvoService = Retrofit.Builder()
        // FIXME: Make that part more modular to plug with other remote data sources!
        .baseUrl(ForvoService.BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(Item::class.java, P())
                    .create()
            ))
        .build()
        .create(ForvoService::class.java),
    wordDatabase = WordDatabase.getDatabase(application)
)