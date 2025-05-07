//package com.dualand.app.viewmodels
//
//class DuaViewModel : ViewModel() {
//    private val _currentIndex = mutableStateOf(0)
//    val currentIndex: State<Int> = _currentIndex
//
//    private val _playbackState = mutableStateOf(PlaybackState())
//    val playbackState: State<PlaybackState> = _playbackState
//
//    private val audioPlayerManager = AudioPlayerManager()
//
//    fun playDua(dua: Dua) {  }
//    fun stopPlayback() {  }
//    fun playWordByWord(dua: Dua) {  }
//    fun toggleRepeat() {  }
//    fun navigateToNextDua() {  }
//    fun navigateToPreviousDua() {  }
//}
//
//data class PlaybackState(
//    val isPlaying: Boolean = false,
//    val currentWordIndex: Int = -1,
//    val showListening: Boolean = false,
//    val repeatCount: Int = 0,
//    val isRepeatMode: Boolean = false
//)
