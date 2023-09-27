package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp
import com.polendina.knounce.ui.theme.SearchFieldFontStyle

@Composable
fun DisplayCard(
    text: String,
    onCardClick: () -> Unit = {},
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCardClick
            )
    ) {
        Text(
            text = text,
            style = SearchFieldFontStyle(),
            color = color,
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
    onSrcCardClick: () -> Unit,
    srcWordDisplay: String,
    targetWordDisplay: String,
    history: (String) -> Unit,
    onExpandedCardSwipe: (Int) -> Unit,
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
            copyTextCallback = history,
            playAudioCallback = playSrcLanguage,
            firstImageVector = Icons.Default.History,
            secondImageVector = Icons.Default.PlayCircleOutline,
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalPager(state = horizontalPagerState) {
            DisplayCard(
                text = srcWordDisplay,
                onCardClick = onSrcCardClick,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(
                        weight = 1f,
                        fill = false
                    )
                    .onPlaced { focusState ->
                        onExpandedCardSwipe(it)
                    }
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
            text = targetWordDisplay,
            copyTextCallback = copyTargetLanguage,
            playAudioCallback = playTargetLanguage,
            firstImageVector = Icons.Default.ContentCopy,
            secondImageVector = Icons.Default.PlayCircleOutline,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        DisplayCard(
            text = targetWordDisplay,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .weight(
                    weight = 1f,
                    fill = false
                )
        )
    }
}