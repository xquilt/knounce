package com.polendina.knounce.presentation.pronunciationsscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.domain.model.Pronunciations
import com.polendina.knounce.presentation.pronunciationsscreen.LanguageSelected

@Composable
fun LanguagesRow(
    languages: List<LanguageSelected>,
    onLanguageBoxClick: (LanguageSelected) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        items(languages) {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .background(if (it.selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                    .padding(
                        vertical = 0.dp,
                        horizontal = 20.dp
                    )//secondaryContainer
                    .clickable {
                        onLanguageBoxClick(it)
                    }
            ) {
                Text(
                    text = it.name,
                    style = TextStyle(
                        color = if (it.selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguagesRowPreview() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        LanguagesRow(
            languages = listOf("Arabic", "English", "French", "Spanish", "Interlingua").map { LanguageSelected(it, it.contains("i").not() ) },
            onLanguageBoxClick = {}
        )
    }
}

