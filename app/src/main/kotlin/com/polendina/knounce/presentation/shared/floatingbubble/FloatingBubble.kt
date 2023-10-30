package com.polendina.knounce.presentation.shared.floatingbubble

import android.app.Service
import android.content.ClipboardManager
import android.content.Intent
import android.os.IBinder
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.app.NotificationCompat
import com.polendina.knounce.R
import com.polendina.knounce.data.database.DatabaseImpl
import com.torrydo.floatingbubbleview.CloseBubbleBehavior
import com.torrydo.floatingbubbleview.FloatingBubbleListener
import com.torrydo.floatingbubbleview.helper.NotificationHelper
import com.torrydo.floatingbubbleview.helper.ViewHelper
import com.torrydo.floatingbubbleview.service.expandable.BubbleBuilder
import com.torrydo.floatingbubbleview.service.expandable.ExpandableBubbleService
import com.torrydo.floatingbubbleview.service.expandable.ExpandedBubbleBuilder

class FloatingBubbleService(
): ExpandableBubbleService() {
    private lateinit var floatingBubbleViewModel: FloatingBubbleViewModel
    override fun onCreate() {
        super.onCreate()
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        floatingBubbleViewModel = FloatingBubbleViewModel(
            application = application,
            database = DatabaseImpl(application)
        )  // TODO: having the viewModel instantiated here, because I wanted to access the System clipboard service, to get the copied text (I couldn't access the CLIPBOARD_SERVICE from within the viewModel).
        clipboardManager.addPrimaryClipChangedListener {
            val copiedString = clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
            floatingBubbleViewModel.srcWord = TextFieldValue(
                text = copiedString,
                selection = TextRange(copiedString.length)
            )
            floatingBubbleViewModel.searchWord(word = copiedString)
        }
        minimize()
    }
    override fun configBubble(): BubbleBuilder {
        return BubbleBuilder(this)
            .bubbleCompose {
                BubbleCompose(
                    expand = { expand() },
                )
            }
            .bubbleStyle(null)
            .startLocation(0, 600)
            .startLocationPx(0, 600)
            .enableAnimateToEdge(true)
            .closeBubbleView(ViewHelper.fromDrawable(this, R.drawable.ic_close_icon, 60, 60))
            .closeBubbleStyle(null)
            .closeBehavior(CloseBubbleBehavior.FIXED_CLOSE_BUBBLE)
            .distanceToClose(100)
            .bottomBackground(false)
            .forceDragging(true)
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
                    minimize = {
                        minimize()
                    },
                    floatingBubbleViewModel = floatingBubbleViewModel
                )
            }
            .startLocation(0, 0)
            .draggable(false)
            .style(null)
            .fillMaxWidth(true)
            .enableAnimateToEdge(true)
            .dimAmount(0.4f)
    }
    override fun startNotificationForeground() {
        val appName = getString(R.string.app_name)
        NotificationHelper(this, appName).apply {
            createNotificationChannel()
        }.let {
            startForeground(
                it.notificationId,
                NotificationCompat.Builder(this, appName)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(getString(R.string.notification_text))
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setSilent(true)
                    .build()
            )
        }
    }

    override fun onDestroy() {
        super.removeAll()
    }
}

class HandleSelection: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val highlightedText = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
        val highlightedTextReadonly = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT_READONLY)
        return super.onStartCommand(intent, flags, startId)
    }
}