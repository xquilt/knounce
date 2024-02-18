package com.polendina.knounce.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polendina.knounce.R
import com.polendina.knounce.presentation.shared.CustomSearchBar

@Composable
internal fun HistoryScreenTopBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    moreCallback: () -> Unit,
    navigateCallback: () -> Unit,
    count: Int
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // TODO: Remove the padding from the sideways
            IconButton(
                onClick = navigateCallback,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.history),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                    )
                )
                Spacer(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                )
                Text(
                    text = "${count} ${stringResource(id = R.string.translations)}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }
            IconButton(
                onClick = moreCallback
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .padding(5.dp)
                )
            }
        }
        CustomSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onActiveChange = {},
            onSearch = { },
            onClearText = {
                onSearchQueryChange("")
            }
        )
    }
}