package com.polendina.knounce.presentation.flashcard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polendina.knounce.data.database.WordDb
import com.polendina.knounce.presentation.history.components.HistoryScreenViewModel
import com.polendina.knounce.presentation.history.components.HistoryScreenViewModelMock
import com.polendina.knounce.ui.theme.KnounceTheme

interface FlashCardScreenViewModel { }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashCardScreen(
    historyScreenViewModel: HistoryScreenViewModel
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
            val horizontalPagerState = rememberPagerState (initialPage = 0) { historyScreenViewModel.wordList.size }
            HorizontalPager(
                state = horizontalPagerState,
                pageSpacing = 10.dp
            ) {
                CardContent(
                    word = HistoryScreenViewModelMock().wordList[it],
                )
            }
        }
    }
}

@Composable
fun CardContent(
    word: WordDb,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .height(500.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Column (
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Text(
                text = word.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 40.sp,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashCardScreenPreview() {
    KnounceTheme {
        FlashCardScreen(
            historyScreenViewModel = HistoryScreenViewModelMock()
        )
    }
}