package com.polendina.knounce.presentation.pronunciationsscreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.polendina.knounce.presentation.pronunciationsscreen.components.homescreen.PronunciationCard
import com.polendina.knounce.presentation.pronunciationsscreen.pronunciations

@Composable
fun PronunciationsScreen(
    modifier: Modifier = Modifier
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchField(
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        LazyColumn {
            items(pronunciations) {
                PronunciationCard(pronunciation = it)
            }
        }
    }

}

