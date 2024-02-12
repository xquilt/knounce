package com.polendina.knounce.data.repository

import com.polendina.knounce.data.database.Word
import com.polendina.knounce.data.database.WordDatabase
import com.polendina.knounce.data.network.PronunciationService
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.domain.repository.KnounceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class KnounceRepositoryImpl(
    private val forvoService: PronunciationService,
    private val wordDatabase: WordDatabase
): KnounceRepository {
    // TODO: Hit the local database first before attempting to load the word from the remote network/or the vice versa (if there's no internet connection)!
    override suspend fun wordPronunciations(
        word: String,
        languageCode: String,
        interfaceLanguageCode: String,
    ): Pronunciations? =
        forvoService.wordPronunciations(
            word = word,
            languageCode = languageCode,
            interfaceLanguageCode = interfaceLanguageCode,
            includePhrases = false
        )
    // TODO: It should accept some (optional) form of search criteria to narrow down the scope (If needed)
    override suspend fun loadWords(): List<Word>? {
        // TODO: Implement a remote repository solution to save the data to the cloud, then populate the local database with them!
        return wordDatabase.wordDao.getWords()
    }
    override suspend fun insertWord(word: Word) {
        wordDatabase.wordDao.insertWord(word = word).also { println(1) }
    }
    override suspend fun removeWord(word: String) {
        wordDatabase.wordDao.deleteWord(word = word)
    }
    // TODO: There gotta be a better approach to load than that!
    override var words: List<Word> = runBlocking(Dispatchers.IO) { wordDatabase.wordDao.getWords() }
    // TODO: Add an additional method to update the local database of words!
}