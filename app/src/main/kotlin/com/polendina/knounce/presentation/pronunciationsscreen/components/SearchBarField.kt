package com.polendina.knounce.presentation.pronunciationsscreen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(modifier: Modifier = Modifier) {
    SearchBar(
        query = "Search",
        onQueryChange = {},
        onSearch = {},
        active = true,
        onActiveChange = {},
        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
            }
        }
    ) {
    }
}

