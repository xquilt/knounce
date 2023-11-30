package com.polendina.knounce.presentation.shared.floatingbubble

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.data.database.DatabaseMock
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.presentation.shared.floatingbubble.components.SearchWordExpandedComposable
import com.polendina.knounce.presentation.shared.floatingbubble.viewModel.FloatingBubbleViewModel
import com.polendina.knounce.presentation.shared.floatingbubble.viewModel.FloatingBubbleViewModelMock
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandedCompose(
    minimize: () -> Unit = {},
    floatingBubbleViewModel: FloatingBubbleViewModel,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { minimize() }
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp
            )
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
                .weight(
                    weight = 1f,
                    fill = false
                )
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                ExpandedBubbleHeader()
                AnimatedVisibility(visible = floatingBubbleViewModel.expanded) {
                    val horizontalPagerState = rememberPagerState(initialPage = floatingBubbleViewModel.pageIndex) { if (floatingBubbleViewModel.words.isEmpty()) 1 else floatingBubbleViewModel.words.size }
                    ExpandedBubbleBody(
                        word = floatingBubbleViewModel.currentWord,
                        srcWordDisplay = floatingBubbleViewModel.currentWord.title,
                        targetWordDisplay = floatingBubbleViewModel.currentWord.translation,
                        onSrcCardClick = {
                            Log.d("MORE","minimized!")
                            floatingBubbleViewModel.expanded = false
                        },
                        onSrcCardWordClick = {
                            floatingBubbleViewModel.searchWord(it)
                            coroutineScope.launch {
                                horizontalPagerState.animateScrollToPage(floatingBubbleViewModel.pageIndex)
                            }
                        },
                        addWordCallback = {
                            floatingBubbleViewModel.currentWord.let {
                                if (it.loaded) floatingBubbleViewModel.removeWordFromDb(wordTitle = it.title) else floatingBubbleViewModel.insertWordToDb(it)
                                floatingBubbleViewModel.invertLoaded()
                            }
                        },
                        playSrcLanguage = {
                            floatingBubbleViewModel.playAudio(
                                searchTerm = it
                            )
                        },
                        copyTargetLanguage = {},
                        playTargetLanguage = {},
                        // TODO: Have a loading effects for pronunciations chips & translation, till it's retrieved!
                        audios = floatingBubbleViewModel.currentWord.pronunciations ?: emptyList(),
                        audioClicked = {
                            PronunciationPlayer.playRemoteAudio(it.second)
                        },
                        horizontalPagerState = horizontalPagerState
                    )
                    LaunchedEffect(horizontalPagerState) {
                        snapshotFlow{horizontalPagerState.currentPage}.collect { page ->
                            floatingBubbleViewModel.pageIndex = page
                            floatingBubbleViewModel.currentWord = floatingBubbleViewModel.words.getOrNull(floatingBubbleViewModel.pageIndex) ?: Word(title = "", translation = null, pronunciations = null, loaded = false)
                            floatingBubbleViewModel.srcWord = TextFieldValue(
                                text = floatingBubbleViewModel.currentWord.title,
                                selection = TextRange(floatingBubbleViewModel.currentWord.title.length)
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = !floatingBubbleViewModel.expanded) {
                    SearchWordExpandedComposable(
                        srcWord = floatingBubbleViewModel.srcWord,
                        onWordChange = {
                            floatingBubbleViewModel.srcWord = it
                        },
                        goToArrowCallback = {
                            floatingBubbleViewModel.searchWord(
                                word = floatingBubbleViewModel.srcWord.text
                            )
                        },
                        clearArrowCallback = {
                            floatingBubbleViewModel.srcWord = TextFieldValue("")
                        },
                        modifier = Modifier
                            .focusRequester(focusRequester = focusRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    keyboardController?.show()
                                }
                            }
                    )
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
        if (floatingBubbleViewModel.expanded) {
            BubbleDisplayFooter(
                close = {
                    minimize()
                },
                newTranslation = {
                    floatingBubbleViewModel.expanded = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandedComposePreview() {
    ExpandedCompose(
        floatingBubbleViewModel = FloatingBubbleViewModelMock(database = DatabaseMock(
            emptyExplanations = true,
            emptyPronunciations = true
        )),
    )
}