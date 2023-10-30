package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R

@Composable
fun ExpandedBubbleHeader(
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ){
        // TODO: Figure out more appropriate icons
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )
        Row {
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(text = stringResource(id = R.string.translation))
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
    }
}

@Composable
fun BottomButton(
    text: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        colors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier
            .wrapContentWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        )
    }
}

@Composable
fun BubbleDisplayFooter(
    close: () -> Unit = {},
    newTranslation: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.dp
            )
    ) {
        BottomButton(text = stringResource(id = R.string.close), onClick = close)
        BottomButton(text = stringResource(id = R.string.new_translation), onClick = newTranslation)
    }
}