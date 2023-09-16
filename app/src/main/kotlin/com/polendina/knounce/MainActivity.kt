package com.polendina.knounce

import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.polendina.knounce.ui.theme.KnounceTheme
import com.torrydo.floatingbubbleview.service.expandable.BubbleBuilder
import com.torrydo.floatingbubbleview.service.expandable.ExpandableBubbleService
import com.torrydo.floatingbubbleview.service.expandable.ExpandedBubbleBuilder

import com.polendina.knounce.presentation.shared.floatingbubble.BubbleCompose
import com.polendina.knounce.presentation.shared.floatingbubble.ExpandedCompose
import com.torrydo.floatingbubbleview.CloseBubbleBehavior
import com.torrydo.floatingbubbleview.FloatingBubbleListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KnounceTheme {
                val context = LocalContext.current
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    PronunciationsScreen()
                    TextButton(onClick = {
                        startService(Intent(context, MyService::class.java))
                    }) {
                        Text(text = "Open It!")
                    }
                }
            }
        }
        val clipboardManager = getSystemService(ComponentActivity.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.addPrimaryClipChangedListener {
            val copiedString = clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
        }
        val highlightedText = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
        val highlightedTextReadonly = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT_READONLY)
    }
}

class MyService: ExpandableBubbleService() {
    override fun onCreate() {
        super.onCreate()
        minimize()
    }
    override fun configBubble(): BubbleBuilder? {
        return BubbleBuilder(this)
            .bubbleCompose {
                BubbleCompose(
                    expand = { expand() },
                )
            }
            .bubbleStyle(null)
            .startLocation(100, 100)
            .startLocationPx(100, 100)
            .enableAnimateToEdge(true)
            .closeBehavior(CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE)
//            .closeBubbleStyle(CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE.ordinal)
            .distanceToClose(100)
            .bottomBackground(false)
            .forceDragging(false)
            .addFloatingBubbleListener(object: FloatingBubbleListener {
                override fun onFingerDown(x: Float, y: Float) {
                    super.onFingerDown(x, y)
                }
                override fun onFingerMove(x: Float, y: Float) {
                    super.onFingerMove(x, y)
                }
                override fun onFingerUp(x: Float, y: Float) {
                    super.onFingerUp(x, y)
                }
            })
    }
    override fun configExpandedBubble(): ExpandedBubbleBuilder? {
        return ExpandedBubbleBuilder(this)
            .expandedCompose {
                ExpandedCompose(
                    close = {
                        minimize()
                    }
                )
            }
            .startLocation(0, 0)
            .draggable(false)
            .style(null)
            .fillMaxWidth(true)
            .enableAnimateToEdge(true)
            .dimAmount(0.4f)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KnounceTheme { }
}