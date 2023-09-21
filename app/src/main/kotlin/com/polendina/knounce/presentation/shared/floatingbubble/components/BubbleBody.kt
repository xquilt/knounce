package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R
import com.polendina.knounce.ui.theme.SearchFieldColors
import com.polendina.knounce.ui.theme.SearchFieldFontStyle

@Composable
fun SearchWordExpandedComposable(
    srcWord: TextFieldValue,
    onWordChange: (TextFieldValue) -> Unit,
    goToArrow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .height(200.dp)
            .padding(bottom = 10.dp)
    ) {
        TextField(
            value = srcWord,
            placeholder = {
                Text(text = stringResource(id = R.string.new_translation))
            },
            onValueChange = onWordChange,
            colors = SearchFieldColors(),
            textStyle = SearchFieldFontStyle(),
            modifier = modifier
                .height(150.dp)
                .fillMaxWidth()
        )
        if(srcWord.text.isNotEmpty()) {
            Row (
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                IconButton(
                    onClick = goToArrow,
                    modifier = modifier
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
    }
}

@Preview(showBackground = true)
@Composable
fun SearchWordExpandedComposablePreview() {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    SearchWordExpandedComposable(
        srcWord = textFieldValue,
        onWordChange = {
            textFieldValue = it
        },
        goToArrow = { /*TODO*/ }
    )
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
    audios: List<Pair<String, String>>,
    audioClicked: (Pair<String, String>) -> Unit,
    modifier: Modifier = Modifier
) {
    DisplayCard(
        text = srcWord,
        copyTextCallback = copySrcLanguage,
        playAudioCallback = playSrcLanguage,
        onCardClick = onSrcCardClick,
        color = MaterialTheme.colorScheme.onBackground,
        firstImageVector = Icons.Default.Shuffle,
        secondImageVector = Icons.Default.PlayCircleOutline
    )
    LazyRow (
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(10.dp)
            .height(35.dp)
            .fillMaxWidth()
    ) {
        items(audios) {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable {
                        audioClicked(it)
                    }
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                Text(
                    text = it.first,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(
                            horizontal = 10.dp,
                        )
                        .wrapContentSize()
                )
            }
        }
    }
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .height(1.dp))
    DisplayCard(
        text = targetWord,
        copyTextCallback = copyTargetLanguage,
        playAudioCallback = playTargetLanguage,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        firstImageVector = Icons.Default.ContentCopy,
        secondImageVector = Icons.Default.PlayCircleOutline
    )
}