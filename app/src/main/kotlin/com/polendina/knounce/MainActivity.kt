package com.polendina.knounce

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.polendina.knounce.presentation.shared.floatingbubble.FloatingBubbleService
import com.polendina.knounce.ui.theme.KnounceTheme

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
                        if (!Settings.canDrawOverlays(this)) {
                            startActivity(
                                Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + packageName)
                                )
                            )
                        } else {
                            startService(Intent(context, FloatingBubbleService::class.java))
                        }
                    }) {
                        Text(text = "Open It!")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KnounceTheme { }
}