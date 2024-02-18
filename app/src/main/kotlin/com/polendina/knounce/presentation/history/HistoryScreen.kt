package com.polendina.knounce.presentation.history.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R
import com.polendina.knounce.presentation.history.HistoryScreenViewModel
import com.polendina.knounce.presentation.history.HistoryScreenViewModelPreviewParameterProvider

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    historyScreenViewModel: HistoryScreenViewModel
) {
    Scaffold (
        topBar = {
            HistoryScreenTopBar(
                searchQuery = historyScreenViewModel.query,
                onSearchQueryChange = historyScreenViewModel::onSearchQuery,
                moreCallback = {},
                count = historyScreenViewModel.words.size,
                navigateCallback = {}
            )
        },
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = historyScreenViewModel.words.isEmpty()
        ) {
            Text(text = stringResource(id = R.string.no_matches))
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(it)
        ) {
            items(items = historyScreenViewModel.words) {
                HistoryItem(
                    word = it,
                    moreCallback = { }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun HistoryItemsPreview(
    @PreviewParameter(
        provider = HistoryScreenViewModelPreviewParameterProvider::class,
        limit = 2
    ) historyScreenViewModel: HistoryScreenViewModel
) {
    HistoryScreen(
        // TODO: The following viewmodel should be renamed to something more appropriate.
        historyScreenViewModel = historyScreenViewModel,
        modifier = Modifier
            .padding(
//                vertical = 20.dp,
                horizontal = 5.dp
            )
    )
}