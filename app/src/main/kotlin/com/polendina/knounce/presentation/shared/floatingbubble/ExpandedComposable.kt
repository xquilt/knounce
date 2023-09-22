package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polendina.knounce.PronunciationPlayer
import com.polendina.knounce.presentation.shared.floatingbubble.components.SearchWordExpandedComposable

@Composable
fun ExpandedCompose(
    minimize: () -> Unit = {},
    floatingBubbleViewModel: FloatingBubbleViewModel,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                minimize()
            }
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp
            )
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {}
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
                    ExpandedBubbleBody(
                        srcWord = floatingBubbleViewModel.srcWordDisplay,
                        targetWord = floatingBubbleViewModel.targetWordDisplay,
                        onSrcCardClick = {
                            floatingBubbleViewModel.expanded = false
                        },
                        history = {
                        },
                        playSrcLanguage = {
                            floatingBubbleViewModel.playAudio(
                                searchTerm = it
                            )
                        },
                        copyTargetLanguage = {},
                        playTargetLanguage = {},
                        audios = floatingBubbleViewModel.loadedPronunciations.get(floatingBubbleViewModel.srcWordDisplay) ?: emptyList(),
                        audioClicked = {
                            PronunciationPlayer.playRemoteAudio(it.second)
                        }
                    )
                }
                AnimatedVisibility(visible = !floatingBubbleViewModel.expanded) {
                    SearchWordExpandedComposable(
                        srcWord = floatingBubbleViewModel.srcWord,
                        onWordChange = {
                            floatingBubbleViewModel.srcWord = it
                        },
                        goToArrowCallback = {
                            floatingBubbleViewModel.expanded = true
                            floatingBubbleViewModel.srcWordDisplay = floatingBubbleViewModel.srcWord.text
                            floatingBubbleViewModel.translateWord()
                            floatingBubbleViewModel.loadPronunciations(
                                searchTerm = floatingBubbleViewModel.srcWordDisplay
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
        floatingBubbleViewModel = viewModel()
    )
}