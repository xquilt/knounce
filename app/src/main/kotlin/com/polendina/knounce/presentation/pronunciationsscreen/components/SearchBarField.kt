package com.polendina.knounce.presentation.pronunciationsscreen.components

import android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    query: String,
    onActiveChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                style = TextStyle(
                    color = Color.Gray
                ),
                modifier = Modifier
                    .height(40.dp)
            )
        },
        onSearch = onSearch,
        active = true,
        onActiveChange = onActiveChange,
        trailingIcon = {
            IconButton(onClick = onTrailingIconClick) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .height(50.dp)
    ) {
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchFieldPreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchField(
            query = "",
            onActiveChange = {},
            onSearch = {},
            onQueryChange = {},
            onTrailingIconClick = { /*TODO*/ },
            modifier = Modifier
//                .height(90.dp)
//                .height(80.dp)
                .padding(10.dp)
        )
    }
}