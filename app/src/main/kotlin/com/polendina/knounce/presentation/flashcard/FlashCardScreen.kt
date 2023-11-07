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
import com.polendina.knounce.ui.theme.KnounceTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashCardScreen(
    mainViewModel: FlashCardViewModel
) {
    val horizontalPagerState = rememberPagerState (initialPage = 0) { mainViewModel.words.size }
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
                    contentDescription = null
                )
                 WordsProgressBar(wordIndex = horizontalPagerState.currentPage, totalWordsCount = mainViewModel.words.size)
                 Row {
                     Icon(imageVector = Icons.Default.Alarm, contentDescription = null)
                     Text(text = "3'15\"")
                 }
             }
        },
        bottomBar = {
            FlashCardBottomBar()
        },
        containerColor = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(10.dp)
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AnimatedVisibility(visible = mainViewModel.words.isNotEmpty()) {
                HorizontalPager(
                    state = horizontalPagerState,
                    pageSpacing = 10.dp
                ) {
                    CardContent(
                        word = mainViewModel.words[it],
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashCardScreenPreview() {
    KnounceTheme {
        FlashCardScreen(
            mainViewModel = FlashCardViewModelMock(database = DatabaseMock())
        )
    }
}