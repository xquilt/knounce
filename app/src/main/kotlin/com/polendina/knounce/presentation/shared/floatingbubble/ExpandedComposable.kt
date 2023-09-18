package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExpandedCompose(
    close: () -> Unit = {},
    floatingBubbleViewModel: FloatingBubbleViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                close()
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
                .width(350.dp)
                .wrapContentHeight()
                .clickable {}
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                ExpandedBubbleHeader()
                if (floatingBubbleViewModel.expanded) {
                    ExpandedBubbleBody(
                        srcWord = floatingBubbleViewModel.srcWord,
                        targetWord = floatingBubbleViewModel.targetWord,
                        onSrcCardClick = {
                            floatingBubbleViewModel.expanded = false
                        },
                        copySrcLanguage = {},
                        playTargetLanguage = {},
                        copyTargetLanguage = {},
                        playSrcLanguage = {
                            floatingBubbleViewModel.just(it)
                        }
                    )
                } else {
                    SearchWordExpandedComposable(
                        srcWord = floatingBubbleViewModel.srcWord,
                        onChangeChange = {
                            floatingBubbleViewModel.srcWord = it
                        },
                        goToArrow = {
                            floatingBubbleViewModel.expanded = true
                            floatingBubbleViewModel.translateWord()
                        }
                    )
                }
            }
        }
        if (floatingBubbleViewModel.expanded) {
            ExpandedBubbleFooter(
                close = {
                    close()
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