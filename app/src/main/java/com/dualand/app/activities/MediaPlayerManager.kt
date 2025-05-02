package com.dualand.app.activities

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

object MediaPlayerManager {

    private var mediaPlayer: MediaPlayer? = null

    fun playAudio(context: Context, resId: Int) {
        stopAudio() // Prevent overlapping audio

        mediaPlayer = MediaPlayer.create(context, resId).apply {
            setOnCompletionListener {
                stopAudio()
            }
            start()
        }
    }

    fun stopAudio() {
        mediaPlayer?.let {
            Log.d("MediaPlayerManager", "Is playing: ${it.isPlaying}")
            if (it.isPlaying) {
                Log.d("MediaPlayerManager", "Stopping audio...")
                it.stop()
            }
            it.reset()
            it.release()
            mediaPlayer = null
            Log.d("MediaPlayerManager", "MediaPlayer stopped, reset, and released.")
        } ?: run {
            Log.d("MediaPlayerManager", "No mediaPlayer to stop.")
        }
    }


    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}
