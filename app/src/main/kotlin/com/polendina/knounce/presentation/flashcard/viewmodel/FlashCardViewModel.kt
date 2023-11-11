package com.polendina.knounce.presentation.flashcard.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.polendina.knounce.data.database.Database
import com.polendina.knounce.data.database.Word

interface FlashCardViewModel: Database {
    val words: SnapshotStateList<Word>
    var elapsedSeconds: Long
}