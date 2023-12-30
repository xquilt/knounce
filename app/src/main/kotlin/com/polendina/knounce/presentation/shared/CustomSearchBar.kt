package com.polendina.knounce.presentation.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    onClearText: () -> Unit,
) {
    SearchBar(
        leadingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search)
            )
        },
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = true,
        onActiveChange = onActiveChange,
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = EnterTransition.None,
                exit = ExitTransition.None
            ) {
                IconButton(onClick = onClearText) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircleOutline,
                        contentDescription = null
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
    }
}