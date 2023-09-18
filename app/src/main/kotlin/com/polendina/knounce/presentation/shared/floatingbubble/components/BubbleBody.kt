package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R
import com.polendina.knounce.ui.theme.SearchFieldColors
import com.polendina.knounce.ui.theme.SearchFieldFontStyle

@Composable
fun SearchWordExpandedComposable(
    srcWord: String,
    onChangeChange: (String) -> Unit,
    goToArrow: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = srcWord,
        placeholder = {
            Text(text = stringResource(id = R.string.new_translation))
        },
        onValueChange = onChangeChange,
        colors = SearchFieldColors(),
        textStyle = SearchFieldFontStyle(),
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    )
    Row (
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        IconButton(
            onClick = goToArrow,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ExpandedBubbleBody(
    onSrcCardClick: () -> Unit,
    srcWord: String,
    targetWord: String,
    copySrcLanguage: (String) -> Unit,
    playSrcLanguage: (String) -> Unit,
    copyTargetLanguage: (String) -> Unit,
    playTargetLanguage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    DisplayCard(
        text = srcWord,
        copyTextCallback = copySrcLanguage,
        playAudioCallback = playSrcLanguage,
        onCardClick = onSrcCardClick,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .height(1.dp))
    DisplayCard(
        text = targetWord,
        copyTextCallback = copySrcLanguage,
        playAudioCallback = playTargetLanguage,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}