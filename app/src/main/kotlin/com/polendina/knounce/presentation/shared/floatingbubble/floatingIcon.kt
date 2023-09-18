package com.polendina.knounce.presentation.shared.floatingbubble

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R
import kotlinx.coroutines.runBlocking

@Composable
fun BubbleCompose(
    show: () -> Unit = {},
    hide: () -> Unit = {},
    animateToEdge: () -> Unit = {},
    expand: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(65.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
//                .fillMaxSize()
                .clickable { runBlocking { expand() } }
        )
    }

//    LaunchedEffect(Unit){
//        while(true){
//            if(index >=2){
//                index = 0
//            }else index++
//            delay(1000)
//        }
//    }

//        Box(
//            modifier = Modifier
//                .pointerInput(Unit) {
//                    detectTapGestures(
//                        onLongPress = {
//                            Toast
//                                .makeText(context, "Long click: Copied \"${songs[index]}\"", Toast.LENGTH_LONG)
//                                .show()
//                        }
//                    )
//                }
//            ,
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = songs[index],
//                fontWeight = FontWeight.Bold,
//            )
//        }

}

@Preview(showBackground = true)
@Composable
fun BubbleComposePreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        BubbleCompose()
    }
}