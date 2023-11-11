package com.polendina.knounce.presentation.flashcard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polendina.knounce.data.database.DatabaseMock
import com.polendina.knounce.data.database.Word
import com.polendina.knounce.presentation.flashcard.viewmodel.FlashCardViewModelMock

@Composable
fun CardContent(
    word: Word,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier
            .height(500.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(0.dp, Color.White, RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            var currentWordType by remember { mutableStateOf(word.translation?.keys?.first() ?: "") }
            Text(
                text = word.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 40.sp,
                )
            )
            word.translation?.forEach {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                        .wrapContentSize()
                        .padding(5.dp)
                        .clickable {
                            currentWordType = it.key
                        }
                ) {
                    Text(
                        text = it.key,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 10.sp,
                        )
                    )
                }
            }
            Divider()
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ){
                Text(
                    text = "Example"
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(word.translation?.get(currentWordType) ?: emptyList()) {index, wordTranslation ->
                    var showTranslation by remember { mutableStateOf(false) }
                    var explanationExpanded by remember { mutableStateOf(false) }
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    showTranslation = !showTranslation
                                    explanationExpanded = !explanationExpanded
                                }
                            )
                    ) {
                        Box (
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(25.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                        ) {
                            Text(
                                text = index.toString(),
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                        Text(
                            text = wordTranslation.explanation ?: "",
                            maxLines = if (!explanationExpanded) 1 else Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        )
                    }
                    AnimatedVisibility(visible = showTranslation) {
                        Column {
                            wordTranslation.examples?.forEach {
                                Text(
                                    text = "\"${it}\"",
                                    style = TextStyle(
                                        color = Color.Gray
                                    ),
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 25.dp
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardContentPreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.outline)
            .padding(10.dp)
    ){
        val words = FlashCardViewModelMock(DatabaseMock()).words
        CardContent(word = words.random())
    }
}