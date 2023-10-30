package com.polendina.knounce.presentation.flashcard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polendina.knounce.data.database.Word

@Composable
fun CardContent(
    word: Word?,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .height(500.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        word?.let {
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
}