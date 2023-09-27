package com.polendina.knounce.presentation.shared.floatingbubble.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R
import com.polendina.knounce.ui.theme.SearchFieldColors
import com.polendina.knounce.ui.theme.SearchFieldFontStyle

@Composable
fun SearchWordExpandedComposable(
    srcWord: TextFieldValue,
    onWordChange: (TextFieldValue) -> Unit,
    goToArrowCallback: () -> Unit,
    clearArrowCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .height(200.dp)
            .padding(bottom = 10.dp)
    ) {
        TextField(
            value = srcWord,
            placeholder = {
                Text(text = stringResource(id = R.string.new_translation))
            },
            onValueChange = onWordChange,
            colors = SearchFieldColors(),
            textStyle = SearchFieldFontStyle(),
            modifier = modifier
                .height(150.dp)
                .fillMaxWidth()
        )
        if(srcWord.text.isNotEmpty()) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                BottomSearchboxIcon(
                    onClick = clearArrowCallback,
                    imageVector = Icons.Default.ClearAll,
                    backgroundColor = MaterialTheme.colorScheme.error,
                    tint = MaterialTheme.colorScheme.onError
                )
                BottomSearchboxIcon(
                    onClick = goToArrowCallback,
                    imageVector = Icons.Default.ArrowForward,
                    backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun BottomSearchboxIcon(
    onClick: () -> Unit,
    imageVector: ImageVector,
    backgroundColor: Color,
    tint: Color,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = backgroundColor,
            contentColor = tint
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchWordExpandedComposablePreview() {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("Nacht")) }
    SearchWordExpandedComposable(
        srcWord = textFieldValue,
        onWordChange = {
            textFieldValue = it
        },
        goToArrowCallback = { /*TODO*/ },
        clearArrowCallback = {}
    )
}