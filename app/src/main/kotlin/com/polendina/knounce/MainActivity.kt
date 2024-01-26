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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.polendina.knounce.data.repository.PronunciationsRepositoryImpl
import com.polendina.knounce.presentation.pronunciationsscreen.PronunciationsViewModel
import com.polendina.knounce.presentation.pronunciationsscreen.components.PronunciationsScreen
import com.polendina.knounce.presentation.pronunciationsscreen.pronunciationsRepositoryImpl
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
                    var switchEnabled by remember { mutableStateOf(true) }
                    PronunciationsScreen(
                        switchEnabled = switchEnabled,
                        pronunciationsViewModel = PronunciationsViewModel(
                            application = application,
                            pronunciationRepository = pronunciationsRepositoryImpl(application = application)
                        ),
                        toggleHover = {
                            val intent = Intent(context, FloatingBubbleService::class.java)
                            if (it) {
                                if (!Settings.canDrawOverlays(this)) {
                                    startActivity(
                                        Intent(
                                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package: ${packageName}")
                                        )
                                    )
                                } else {
                                    startService(intent)
                                    switchEnabled = true
                                }
                            } else {
                                stopService(intent)
                                switchEnabled = false
                            }
                        }
                    )
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