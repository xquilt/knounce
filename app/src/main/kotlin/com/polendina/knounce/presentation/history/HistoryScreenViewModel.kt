package com.polendina.knounce.presentation.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.domain.repository.KnounceRepository

class HistoryScreenViewModel(
    private val knounceRepository: KnounceRepository
): ViewModel() {
    var query by mutableStateOf("")
        private set
    private val _words by lazy { knounceRepository.words }
    val words: SnapshotStateList<Word> = mutableStateListOf<Word>().also { it.addAll(_words) }

    fun onSearchQuery(newQuery: String) {
        query = newQuery
        words.clear()
        if (newQuery.isNotBlank()) {
            words.addAll(_words.filter { word -> word.title.contains(newQuery) })
        } else {
            words.addAll(_words.toMutableStateList())
        }
    }
}