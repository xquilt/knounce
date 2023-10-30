package com.polendina.knounce.presentation.flashcard

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
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.data.database.DatabaseMock
import com.polendina.knounce.presentation.flashcard.components.CardContent
import com.polendina.knounce.presentation.flashcard.viewmodel.FlashCardViewModel
import com.polendina.knounce.presentation.flashcard.viewmodel.FlashCardViewModelMock
import com.polendina.knounce.ui.theme.KnounceTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashCardScreen(
    mainViewModel: FlashCardViewModel
) {
    Scaffold (
        topBar = {
             Row (
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(40.dp)
             ){
                Icon(
                    imageVector = Icons.Default.RemoveCircle,
                    contentDescription = null
                )
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
        ){
            val horizontalPagerState = rememberPagerState (initialPage = 0) { mainViewModel.words.size }
            HorizontalPager(
                state = horizontalPagerState,
                pageSpacing = 10.dp
            ) {
                CardContent(
                    word = mainViewModel.words.getOrNull(it),
                )
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