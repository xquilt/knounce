package com.polendina.knounce

import android.app.Application
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService

/**
 * Companion object responsible for playing remote audio files.
*/
object PronunciationPlayer {

    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    /**
     * Attempt to play remote audio files
     *
     * @param url Direct URL of the remote audio file.
    */
    fun playRemoteAudio(url: String) {
        mediaPlayer.apply {
            reset()
            setDataSource(url)
            prepare()
            start()
        }
    }

    private val audioQueue = mutableListOf<String>()

    /**
     * Queue up multiple audio files to be played.
     *
     * @param url Direct URL of the remote audio file.
     */
    fun playRemoteAudioQueued(url: String) {
        audioQueue.add(url)
        if (!mediaPlayer.isPlaying) playRemoteAudio(url = url)
        mediaPlayer.setOnCompletionListener {
            audioQueue.remove(url)
            audioQueue.firstOrNull()?.let {
                playRemoteAudio(url = it)
            }
        }
    }

    /**
     * Pause currently running audio file.
     *
     * @return A Boolean value indicating whether it's successfully paused or not.
    */
    fun pauseAudio(): Boolean {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            return (true)
        }
        return (false)
    }

    /**
     * Resume playing of the audio file.
     *
     * @return A Boolean value indicating whether it's successfully resumed or not.
     */
    fun resumeAudio(): Boolean {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            return (true)
        }
        return false
    }

}

// TODO: I still have no idea about where this should be appropriately placed
class NetworkHandler(private val application: Context) {
    private val connectivityManager = application.applicationContext.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
    fun isNetworkAvailable(): Boolean {
        var result = false
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return (result)
    }

}
