package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.ui.theme.SearchFieldFontStyle
import com.polendina.knounce.utils.wordByCharIndex

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayCard(
    text: String,
    onCardClick: () -> Unit = {},
    onSrcCardWordClick: (String) -> Unit = {},
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 140.dp)
            .combinedClickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCardClick,
                onDoubleClick = onCardClick,
                onLongClick = onCardClick
            )
    ) {
        ClickableText(
            text = AnnotatedString(text),
            style = SearchFieldFontStyle(),
            onClick = {offset ->
                onSrcCardWordClick(text.wordByCharIndex(index = offset))
            },
//            color = color, // FIXME: I have no idea how to set color for AnnotatedString
            modifier = Modifier
                .verticalScroll(rememberScrollState(0))
        )
    }
}

@Composable
fun MediaControlsRow(
    text: String,
    copyTextCallback: (String) -> Unit = {},
    playAudioCallback: (String) -> Unit = {},
    firstImageVector: ImageVector,
    secondImageVector: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        IconButton(onClick = { copyTextCallback(text) }) {
            Icon(
                imageVector = firstImageVector,
                contentDescription = null,
                tint = color
            )
        }
        IconButton(onClick = { playAudioCallback(text) }) {
            Icon(
                imageVector = secondImageVector,
                contentDescription = null,
                tint = color
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandedBubbleBody(
    word: Word,
    onSrcCardClick: () -> Unit,
    onSrcCardWordClick: (String) -> Unit,
    srcWordDisplay: String,
    targetWordDisplay: MutableMap<String, MutableList<Word.Translation>>?,
    addWordCallback: (String) -> Unit,
    playSrcLanguage: (String) -> Unit,
    copyTargetLanguage: (String) -> Unit,
    playTargetLanguage: (String) -> Unit,
    audios: List<Pair<String, String>>,
    audioClicked: (Pair<String, String>) -> Unit,
    horizontalPagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Column {
        MediaControlsRow(
            text = srcWordDisplay,
            copyTextCallback = addWordCallback,
            playAudioCallback = playSrcLanguage,
            firstImageVector = if (word.loaded) Icons.Default.CheckCircle else Icons.Default.AddCircle, // TODO: Probably it's better to have some long-press functionality on the src word display portion, that preview a deletion prompt of some kind.
            secondImageVector = Icons.Default.PlayCircleOutline,
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalPager(state = horizontalPagerState) {
            DisplayCard(
                text = srcWordDisplay,
                onCardClick = onSrcCardClick,
                onSrcCardWordClick = onSrcCardWordClick,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(
                        weight = 1f,
                        fill = false
                    )
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .height(35.dp)
                .fillMaxWidth()
        ) {
            items(audios) {
                Box(
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
        MediaControlsRow(
            text = targetWordDisplay?.keys?.first() ?: "",
            copyTextCallback = copyTargetLanguage,
            playAudioCallback = playTargetLanguage,
            firstImageVector = Icons.Default.ContentCopy,
            secondImageVector = Icons.Default.PlayCircleOutline,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        DisplayCard(
            text = targetWordDisplay?.values?.first()?.first()?.explanation ?: "",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .weight(
                    weight = 1f,
                    fill = false
                )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun ExpandedBubbleBodyPreview() {
    val word by remember { mutableStateOf(Word(title = "", translation = null, pronunciations = null, loaded = false)) }
    ExpandedBubbleBody(
        word = word,
        onSrcCardClick = { /*TODO*/ },
        onSrcCardWordClick = {},
        srcWordDisplay = "",
        targetWordDisplay = word.translation,
        addWordCallback = {
            word.loaded = !word.loaded
        },
        playSrcLanguage = {},
        copyTargetLanguage = {},
        playTargetLanguage = {},
        audios = emptyList(),
        audioClicked = {},
        horizontalPagerState = rememberPagerState { 1 }
    )
}