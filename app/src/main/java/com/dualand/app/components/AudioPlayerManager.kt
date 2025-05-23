package com.dualand.app.components

import android.content.Context
import android.media.MediaPlayer

class AudioPlayerManager(private val context: Context) {

    private val mediaPlayer = MediaPlayer()
    var isPlayingWordByWord = false
    var isPlayingFullAudio = false
    var highlightedIndex: Int = -1
        private set

    fun stopAllAudio() {
        stopAudio()
        isPlayingWordByWord = false
        isPlayingFullAudio = false
    }

    private fun stopAudio() {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.reset()
        highlightedIndex = -1
    }

    fun playFullDuaAudio(
        audioResId: Int?,
        onComplete: () -> Unit
    ) {
        if (audioResId == null) return
        stopAudio()
        isPlayingWordByWord = false
        isPlayingFullAudio = true

        playAudio(audioResId) {
            isPlayingFullAudio = false
            onComplete()
        }
    }

    fun playWordByWordAudio(
        wordAudioPairs: List<Pair<String, Int>>?,
        onHighlightIndex: (Int) -> Unit,
        onComplete: () -> Unit
    ) {
        if (wordAudioPairs.isNullOrEmpty()) return
        stopAudio()
        isPlayingFullAudio = false
        isPlayingWordByWord = true

        fun playSequentially(index: Int) {
            if (index >= wordAudioPairs.size) {
                isPlayingWordByWord = false
                highlightedIndex = -1
                onComplete()
                return
            }
            val (_, audioRes) = wordAudioPairs[index]
            highlightedIndex = index
            onHighlightIndex(index)

            playAudio(audioRes) {
                playSequentially(index + 1)
            }
        }

        playSequentially(0)
    }

    private fun playAudio(resId: Int, onComplete: () -> Unit) {
        try {
            val afd = context.resources.openRawResourceFd(resId) ?: return
            mediaPlayer.reset()
            mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            mediaPlayer.prepare()
            mediaPlayer.setOnCompletionListener { onComplete() }
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete()
        }
    }
}

