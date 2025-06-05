package com.dualand.app

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dualand.app.activities.DuaDataProvider
import com.dualand.app.models.Dua
import com.dualand.app.models.DuaStatusEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.SortedMap


class DuaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).favoriteDuaDao()
    private val context = application.applicationContext
    val someSettingState = mutableStateOf(false)
    var currentIndex by mutableStateOf(0)
        private set
    fun updateCurrentIndex(index: Int, fromFiltered: Boolean = true) {
        currentIndex = index }
    private val _groupedAndSortedDuas = DuaDataProvider.duaList.groupBy {
        it.duaNumber.substringBefore(".").trim().toInt()
    }.toSortedMap()
    val groupedAndSortedDuas: SortedMap<Int, List<Dua>>
        get() = _groupedAndSortedDuas
    val favouriteDuas: StateFlow<List<DuaStatusEntity>> =
        dao.getAllDuaStatuses()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val currentDua: List<Dua>
        get() = groupedAndSortedDuas[duaKeys.getOrNull(currentIndex)] ?: emptyList()
    var highlightedIndex by mutableStateOf(-1)
        private set
    var isPlayingWordByWord by mutableStateOf(false)
        private set
    var isPlayingFullAudio by mutableStateOf(false)
        private set
    private var mediaPlayer: MediaPlayer? = null
    var autoPlayFavorites by mutableStateOf(false)
    private var favoritePlayIndex = 0
    private var favoriteAutoPlayIndex = 0
    private var favoriteAutoPlayList: List<Int> = emptyList()
    var isFavoriteAutoPlayActive by mutableStateOf(false)
        private set
    fun getFavoriteAutoPlayIndex(): Int = favoriteAutoPlayIndex
    fun getFavoriteAutoPlayListSize(): Int = favoriteAutoPlayList.size
    var isManualStop by mutableStateOf(false)
        private set
    val duaKeys = groupedAndSortedDuas.keys.toList()
    private val _selectedTab = MutableStateFlow("WORD")
    val selectedTab: StateFlow<String> = _selectedTab

    val currentFullAudioDuaGroupIndex = mutableStateOf(-1)
    val currentFullAudioDuaIndexState = mutableStateOf(-1)
    private var pausedAudioPosition: Int = 0
    private var pausedDuaIndexInGroup: Int = -1
    private var pausedGroupIndex: Int = -1
    var repeatCount by mutableStateOf(0)
        private set
    private val _repeatCountsPerDua = mutableStateMapOf<Int, Int>()
    val repeatCountsPerDua: Map<Int, Int> get() = _repeatCountsPerDua
    private val repeatCountersPerDua = mutableMapOf<Int, Int>()
    private var repeatPlayCounter = 0
    var currentDuaIndex by mutableStateOf(0)
    var currentWordIndexInDua by mutableStateOf(-1)
    val currentDuaIndexState = mutableStateOf(-1)
    val isListeningPause = mutableStateOf(false)
    private val _wordRepeatCountsPerDua = mutableStateMapOf<Int, Int>()
    private val wordRepeatCountersPerDua = mutableMapOf<Int, Int>()

    val wordRepeatCountsPerDua: Map<Int, Int>
        get() = _wordRepeatCountsPerDua

    fun setSelectedTab(tab: String) {
        _selectedTab.value = tab

        _repeatCountsPerDua.clear()
        repeatCountersPerDua.clear()
        repeatPlayCounter = 0

        _wordRepeatCountsPerDua.clear()
        wordRepeatCountersPerDua.clear()
    }

    fun resetRepeatStates() {
        repeatPlayCounter = 0
        _repeatCountsPerDua.clear()
        repeatCountersPerDua.clear()
        _wordRepeatCountsPerDua.clear()
        wordRepeatCountersPerDua.clear()
    }


    fun startFavoriteAutoPlay(favoriteDuas: List<Dua>) {
        isManualStop = false
        favoriteAutoPlayList = favoriteDuas.mapNotNull { dua ->
            // Find the group index that contains this dua
            groupedAndSortedDuas.entries.indexOfFirst { it.value.any { it.id == dua.id } }
        }.distinct()

        favoriteAutoPlayIndex = 0
        isFavoriteAutoPlayActive = true
        setSelectedTab("COMPLETE")
        stopAudio()

        if (favoriteAutoPlayList.isNotEmpty()) {
            updateCurrentIndex(favoriteAutoPlayList[0])
        }
    }

    fun stopFavoriteAutoPlay() {
        isFavoriteAutoPlayActive = false
        favoriteAutoPlayList = emptyList()
        favoriteAutoPlayIndex = 0
    }

    fun handleFavoriteAutoPlayDone() {
        if (isManualStop) {
            stopFavoriteAutoPlay()
            return
        }

        favoriteAutoPlayIndex++
        if (favoriteAutoPlayIndex < favoriteAutoPlayList.size) {
            updateCurrentIndex(favoriteAutoPlayList[favoriteAutoPlayIndex])
            currentIndex = favoriteAutoPlayList[favoriteAutoPlayIndex]
        } else {
            isFavoriteAutoPlayActive = false
            //when favourite play completes
            autoPlayFavorites = false
            stopAudio()
        }
    }

    fun nextDua() {
        if (autoPlayFavorites) return

        if (currentIndex < duaKeys.lastIndex) {
            stopAudio()
            currentIndex++
            resetRepeatStates()
        }
    }

    fun previousDua() {
        if (autoPlayFavorites) return

        if (currentIndex > 0) {
            stopAudio()
            currentIndex--
            resetRepeatStates()
        }
    }

    fun pauseFullAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                pausedAudioPosition = it.currentPosition
                pausedDuaIndexInGroup = currentFullAudioDuaIndexState.value
                pausedGroupIndex = currentFullAudioDuaGroupIndex.value
                it.pause()
                isPlayingFullAudio = false
            }
        }
    }

    fun updateRepeatCountForDua(index: Int, count: Int) {
        _repeatCountsPerDua[index] = count
        repeatCountersPerDua[index] = 0
    }

    fun playFullAudio(startIndexInGroup: Int = 0, resume: Boolean = false) {
        stopAudio()
        val duaList = if (autoPlayFavorites) {
            currentDua.filter { dua ->
                favouriteDuas.value.any { it.duaId == dua.id && it.favorite }
            }
        } else {
            currentDua
        }

        if (duaList.isEmpty()) {
            //if (autoPlayFavorites) handleFavoriteAutoPlayDone()
            return
        }

        val indexToStart = if (resume && pausedDuaIndexInGroup >= 0 && pausedGroupIndex == currentIndex) {
            pausedDuaIndexInGroup
        } else {
            startIndexInGroup
        }
        val shouldResume = resume && pausedAudioPosition > 0 &&
                pausedDuaIndexInGroup >= 0 &&
                pausedGroupIndex == currentIndex

        if (shouldResume && !isManualStop) {
            val resId = duaList.getOrNull(pausedDuaIndexInGroup)?.fullAudioResId ?: 0
            if (resId != 0) {
                try {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(context, resId)
                    mediaPlayer?.apply {
                        seekTo(pausedAudioPosition)
                        start()

                        currentFullAudioDuaGroupIndex.value = pausedGroupIndex
                        currentFullAudioDuaIndexState.value = pausedDuaIndexInGroup
                        isPlayingFullAudio = true
                        highlightedIndex = pausedDuaIndexInGroup

                        setOnCompletionListener {
                            release()
                            mediaPlayer = null

                            if (repeatCount == -1 || repeatPlayCounter < repeatCount - 1) {
                                repeatPlayCounter++
                                playFullAudio(indexToStart, resume = false) // 🔁 Repeat same dua
                            } else {
                                repeatPlayCounter = 0
                                repeatCount = 0
                                //playNextAudio(duaList, indexToStart + 1, currentIndex)
                                playNextAudio(duaList, indexToStart + 1)
                            }
                        }
                    }
                    return
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Reset paused values
        pausedAudioPosition = 0
        pausedDuaIndexInGroup = -1
        pausedGroupIndex = -1

        val titleAudio = duaList.firstOrNull()?.titleAudioResId ?: 0
        if (readTitleEnabled.value && titleAudio != 0 && indexToStart == 0) {
            try {
                mediaPlayer = MediaPlayer.create(context, titleAudio)
                mediaPlayer?.apply {
                    start()
                    highlightedIndex = -1
                    currentFullAudioDuaGroupIndex.value = currentIndex

                    setOnCompletionListener {
                        release()
                        mediaPlayer = null
                        // proceed to play actual dua content
                        playNextAudio(duaList, indexToStart)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                playNextAudio(duaList, indexToStart)
            }
        } else {
            playNextAudio(duaList, indexToStart)
        }
    }

    private fun playNextAudio(duaList: List<Dua>, index: Int) {
        if (index >= duaList.size) {
            if (isManualStop) return
            highlightedIndex = -1
            currentFullAudioDuaIndexState.value = -1

            Handler(Looper.getMainLooper()).post {
                currentFullAudioDuaGroupIndex.value = -1
            }

            if (autoPlayFavorites) {
                //Get the index of the next favorite dua

                handleFavoriteAutoPlayDone()
            } else if (autoNextEnabled.value) {
                currentIndex++
                resetRepeatStates()
                playFullAudio()
            }
            return
        }

        //  Skip non-favorites in favorite auto play mode
        if (autoPlayFavorites) {
            val currentDua = duaList[index]
            val status = favouriteDuas.value.find { it.duaId == currentDua.id }
            val isFavorite = status?.favorite == true

            if (!isFavorite) {
                playNextAudio(duaList, index + 1)
                return
            }
        }

        val resId = duaList[index].fullAudioResId
        if (resId == 0) {
            playNextAudio(duaList, index + 1)
            return
        }

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer?.apply {
                start()

                Handler(Looper.getMainLooper()).post {
                    //currentFullAudioDuaGroupIndex.value = duaGroupIndex
                    currentFullAudioDuaGroupIndex.value = currentIndex
                    currentFullAudioDuaIndexState.value = index
                    isPlayingFullAudio = true
                    highlightedIndex = index
                }

                setOnCompletionListener {
                    release()
                    mediaPlayer = null

                    val repeatCountForCurrent = _repeatCountsPerDua[index] ?: 0
                    val currentRepeat = repeatCountersPerDua[index] ?: 0

                    if (repeatCountForCurrent == -1 || currentRepeat < repeatCountForCurrent) {
                        repeatCountersPerDua[index] = currentRepeat + 1
                        playNextAudio(duaList, index)
                    } else {
                        repeatCountersPerDua[index] = 0
                        _repeatCountsPerDua[index] = 0
                        playNextAudio(duaList, index + 1)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            playNextAudio(duaList, index + 1)
        }
    }

    fun playWordByWord(startIndexInGroup: Int = 0) {
        stopAudio()

        if (currentIndex >= duaKeys.size) {
            isPlayingWordByWord = false
            currentDuaIndex = 0
            currentWordIndexInDua = -1
            return
        }

        val duaList = currentDua
        if (duaList.isEmpty()) {
            currentIndex++
            playWordByWord(startIndexInGroup)
            return
        }

        currentDuaIndex = startIndexInGroup

        val firstDua = duaList.getOrNull(startIndexInGroup)
        val titleAudioResId = firstDua?.titleAudioResId

        if (readTitleEnabled.value && titleAudioResId != null && startIndexInGroup == 0) {
            playAudio(titleAudioResId) {
                playDuaSequentially(duaList, startIndexInGroup)
            }
        } else {
            playDuaSequentially(duaList, startIndexInGroup)
        }
    }
    fun playAudio(@RawRes resId: Int, onComplete: () -> Unit) {
       // stopAudio()
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.apply {
            setOnCompletionListener {
                onComplete()
            }
            start()
        }
    }
    private fun playDuaSequentially(duaList: List<Dua>, duaIndex: Int) {
        if (duaIndex >= duaList.size) {
            if (isManualStop) return
            currentWordIndexInDua = -1
            currentDuaIndexState.value = -1

            if (autoNextEnabled.value) {
                currentIndex++
                resetRepeatStates()
                playWordByWord()
            }
            return
        }

        currentDuaIndexState.value = duaIndex
        currentDuaIndex = duaIndex
        currentWordIndexInDua = 0
        isPlayingWordByWord = true

        val currentDua = duaList[duaIndex]
        val wordAudioPairs = currentDua.wordAudioPairs

        playWordsInDua(wordAudioPairs, 0) {
            val repeatCountForCurrent = _wordRepeatCountsPerDua[duaIndex] ?: 0
            val currentRepeat = wordRepeatCountersPerDua[duaIndex] ?: 0

            if (repeatCountForCurrent == -1 || currentRepeat < repeatCountForCurrent) {
                wordRepeatCountersPerDua[duaIndex] = currentRepeat + 1
                playDuaSequentially(duaList, duaIndex)
            } else {
                wordRepeatCountersPerDua[duaIndex] = 0
                _wordRepeatCountsPerDua[duaIndex] = 0
                playDuaSequentially(duaList, duaIndex + 1)
            }
        }
    }
    private fun playWordsInDua(
        pairs: List<Pair<String, Int>>,
        index: Int,
        onFinished: () -> Unit
    ) {
        if (index >= pairs.size || !isPlayingWordByWord) {
            isListeningPause.value = false
            currentWordIndexInDua = -1
            onFinished()
            return
        }

        currentWordIndexInDua = index
        val audioResId = pairs[index].second

        if (audioResId == 0) {
            playWordsInDua(pairs, index + 1, onFinished)
            return
        }

        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null

            mediaPlayer = MediaPlayer.create(context, audioResId)
            mediaPlayer?.apply {
                start()

                setOnCompletionListener {
                    val duration = this.duration.toLong() // actual audio duration

                    release()
                    mediaPlayer = null

                    isListeningPause.value = true

                    val pauseDuration = if (_wordByWordPauseEnabled.value) {
                        _pauseSeconds.value * 1000L // from user setting
                    } else {
                        duration // match audio duration
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        if (!isPlayingWordByWord) {
                            isListeningPause.value = false
                            currentWordIndexInDua = -1
                            return@postDelayed
                        }

                        isListeningPause.value = false
                        playWordsInDua(pairs, index + 1, onFinished)
                    }, pauseDuration)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            playWordsInDua(pairs, index + 1, onFinished)
        }
    }

    fun stopAudio(manual: Boolean = false) {
        if (manual) {
            isManualStop = true
            stopFavoriteAutoPlay()
        }

        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        repeatPlayCounter = 0
        isPlayingFullAudio = false
        isPlayingWordByWord = false
        highlightedIndex = -1
        currentWordIndexInDua = -1
        currentDuaIndex = -1
        currentFullAudioDuaGroupIndex.value = -1
        isListeningPause.value = false
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }

//    // Toggle favorite
//    fun toggleFavorite(duaNumber: String, status: String) {
//        viewModelScope.launch {
//            val isFav = dao.isFavorite(duaNumber)
//            val dua = DuaStatusEntity(duaNumber, !isFav, status)
//            if (isFav) {
//                dao.updateFavoriteStatus(duaNumber, false)
//            } else {
//                dao.insertFavorite(dua)
//            }
//        }
//    }

    fun updateDuaStatus(duaId: Int, newStatus: String) {
        viewModelScope.launch {
            val existingDua = dao.getDuaStatusById(duaId)
            if (existingDua != null) {
                dao.updateDuaStatus(duaId, newStatus)
            } else {
                val newDua = DuaStatusEntity(
                    duaId = duaId,
                    favorite = false,
                    status = newStatus
                )
                dao.insertFavorite(newDua)
            }
        }
    }

    fun ensureAllDuasAreTracked(duaList: List<Dua>) {
        viewModelScope.launch {
            duaList.forEach { dua ->
                val existing = dao.getDuaStatusById(dua.id)
                if (existing == null) {
                    dao.insertFavorite(
                        DuaStatusEntity(
                            duaId = dua.id,
                            favorite = false,
                            status = "In Practice"
                        )
                    )
                }
            }
        }
    }


//    fun convertInPracticeToMemorized() {
//        viewModelScope.launch {
//            allDuas.value.forEach { duaStatus ->
//                if (duaStatus.status == "In Practice") {
//                    updateDuaStatus(
//                        duaNumber = duaStatus.duaNumber,
//                        newStatus = "Memorized"
//                    )
//                }
//            }
//        }
//    }
fun toggleFavoriteStatus(dua: Dua) {
    viewModelScope.launch {
        val id = dua.id
        val existing = dao.getDuaStatusById(id)

        if (existing != null) {
            dao.updateFavoriteStatus(id, !existing.favorite)
        } else {
            dao.insertFavorite(
                DuaStatusEntity(
                    duaId = id,
                    favorite = true,
                    status = "In Practice"
                )
            )
        }
    }
}

//    fun toggleFavoriteStatus(dua: Dua) {
//        viewModelScope.launch {
//            val id = dua.id
//            val isFav = dao.isFavorite(id)
//            dao.updateFavoriteStatus(id, !isFav)
//        }
//    }

    fun isFavorite(duaId: Int): Flow<Boolean> {
        return dao.getAllFavoriteDuas().map { list ->
            list.any { it.duaId == duaId && it.favorite }
        }
    }

    //Settings
    private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _readTitleEnabled = MutableStateFlow(prefs.getBoolean("read_title_enabled", false))
    val readTitleEnabled: StateFlow<Boolean> = _readTitleEnabled

    private val _rewardsEnabled = MutableStateFlow(prefs.getBoolean("rewards_enabled", false))
    val rewardsEnabled: StateFlow<Boolean> = _rewardsEnabled

    private val _autoNextEnabled = MutableStateFlow(prefs.getBoolean("auto_next_duas_enabled", false))
    val autoNextEnabled: StateFlow<Boolean> = _autoNextEnabled

    private val _wordByWordPauseEnabled = MutableStateFlow(prefs.getBoolean("word_by_word_pause_enabled", false))
    val wordByWordPauseEnabled: StateFlow<Boolean> = _wordByWordPauseEnabled

    private val _pauseSeconds = MutableStateFlow(prefs.getInt("word_by_word_pause_seconds", 2))
    val pauseSeconds: StateFlow<Int> = _pauseSeconds

    private val _selectedVoice = MutableStateFlow(prefs.getString("selected_voice", "Female") ?: "Female")
    val selectedVoice: StateFlow<String> = _selectedVoice

    private val _fontSize = MutableStateFlow(prefs.getFloat("font_size", 24f))
    val fontSize: StateFlow<Float> = _fontSize

    fun setReadTitleEnabled(enabled: Boolean) {
        _readTitleEnabled.value = enabled
        prefs.edit().putBoolean("read_title_enabled", enabled).apply()
    }

    fun setRewardsEnabled(enabled: Boolean) {
        _rewardsEnabled.value = enabled
        prefs.edit().putBoolean("rewards_enabled", enabled).apply()
    }

    fun setAutoNextEnabled(enabled: Boolean) {
        _autoNextEnabled.value = enabled
        prefs.edit().putBoolean("auto_next_duas_enabled", enabled).apply()
    }

    fun setWordByWordPauseEnabled(enabled: Boolean) {
        _wordByWordPauseEnabled.value = enabled
        prefs.edit().putBoolean("word_by_word_pause_enabled", enabled).apply()
    }

    fun setPauseSeconds(seconds: Int) {
        _pauseSeconds.value = seconds
        prefs.edit().putInt("word_by_word_pause_seconds", seconds).apply()
    }

    fun setSelectedVoice(gender: String) {
        _selectedVoice.value = gender
        prefs.edit().putString("selected_voice", gender).apply()
    }

    fun setFontSize(size: Float) {
        _fontSize.value = size
        prefs.edit().putFloat("font_size", size).apply()
    }

    fun updateWordRepeatCountForDua(index: Int, count: Int) {
        _wordRepeatCountsPerDua[index] = count
        wordRepeatCountersPerDua[index] = 0
    }


    // Settings Ends
}

