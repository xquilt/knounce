package com.polendina.knounce.presentation.flashcard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.data.database.DatabaseMock
import com.polendina.knounce.presentation.flashcard.components.CardContent
import com.polendina.knounce.presentation.flashcard.components.WordsProgressBar
import com.polendina.knounce.presentation.flashcard.viewmodel.FlashCardViewModel
import com.polendina.knounce.presentation.flashcard.viewmodel.FlashCardViewModelMock
import com.polendina.knounce.utils.formatElapsedLocalTime
import java.time.LocalTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashCardScreen(
    flashCardViewModel: FlashCardViewModel
) {
    val horizontalPagerState = rememberPagerState (initialPage = 0) { flashCardViewModel.words.size }
    Scaffold (
        topBar = {
             Row (
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.SpaceBetween,
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(40.dp)
             ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                 WordsProgressBar(
                     wordIndex = horizontalPagerState.currentPage,
                     totalWordsCount = flashCardViewModel.words.size
                 )
                 Row (
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.spacedBy(2.dp)
                 ){
                     Icon(imageVector = Icons.Default.Alarm, contentDescription = null)
                     Text(
                         text = LocalTime
                             .ofSecondOfDay(flashCardViewModel.elapsedSeconds)
                             .formatElapsedLocalTime()
                     )
                 }
             }
        },
        bottomBar = {
            FlashCardBottomBar()
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(10.dp)
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AnimatedVisibility(visible = flashCardViewModel.words.isNotEmpty()) {
                HorizontalPager(
                    state = horizontalPagerState,
                    pageSpacing = 10.dp
                ) {
                    CardContent(
                        word = flashCardViewModel.words[it],
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashCardScreenPreview() {
    FlashCardScreen(
        flashCardViewModel = FlashCardViewModelMock(database = DatabaseMock())
    )
}