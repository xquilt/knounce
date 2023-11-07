package com.polendina.knounce.presentation.flashcard.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun WordsProgressBar(
    wordIndex: Int,
    totalWordsCount: Int
) {
    val progress by animateFloatAsState(
        targetValue = (wordIndex + 1) / totalWordsCount.toFloat()
    )
    LinearProgressIndicator(
        progress = progress,
        modifier = Modifier
            .height(10.dp)
            .clip(RoundedCornerShape(10.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun WordsProgressBarPreview() {
    Column (
        modifier = Modifier
            .padding(50.dp)
    ) {
        var wordIndex by remember { mutableIntStateOf(3) }
        WordsProgressBar(wordIndex = wordIndex, totalWordsCount = 9)
        Button(onClick = {
            wordIndex += 1
        }) {
        }
    }
}