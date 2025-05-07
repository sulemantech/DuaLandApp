package com.dualand.app.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler


class AudioPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    fun playAudio(context: Context, audioResId: Int, onComplete: () -> Unit = {}) {
        stopPlayback()
        mediaPlayer = MediaPlayer.create(context, audioResId).apply {
            setOnCompletionListener {
                release()
                mediaPlayer = null
                onComplete()
            }
            start()
        }
    }

    fun stopPlayback() {
        mediaPlayer?.release()
        mediaPlayer = null
        handler?.removeCallbacks(runnable ?: Runnable {})
    }

    fun playWordByWord(context: Context, words: List<Pair<String, Int>>, onWordChanged: (Int) -> Unit) {
        // Implement word-by-word playback
    }
}