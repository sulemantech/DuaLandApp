package com.dualand.app

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dualand.app.activities.DuaDataProvider
import com.dualand.app.models.Dua
import com.dualand.app.models.DuaStatusEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DuaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).favoriteDuaDao()

    var globalWordIndex by mutableStateOf(-1)
    var currentPlayingIndex by mutableStateOf(-1)


    private val context = application.applicationContext

    val someSettingState = mutableStateOf(false)

    fun toggleSomeSetting() {
        someSettingState.value = !someSettingState.value
    }

    var currentIndex by mutableStateOf(0)
        private set

    var highlightedIndex by mutableStateOf(-1)
        private set

    var isPlayingWordByWord by mutableStateOf(false)
        private set

    var isPlayingFullAudio by mutableStateOf(false)
        private set

    private var mediaPlayer: MediaPlayer? = null

    // Group and sort the dua list
    private val groupedAndSortedDuas = DuaDataProvider.duaList.groupBy {
        it.duaNumber.substringBefore(".").trim().toInt()
    }.toSortedMap()

    // Get all favorites
    val favoriteDuas: StateFlow<List<DuaStatusEntity>> = dao.getAllFavoriteDuas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Get all statuses
    val allDuas: StateFlow<List<DuaStatusEntity>> = dao.getAllDuaStatuses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val duaKeys = groupedAndSortedDuas.keys.toList()

    private val _selectedTab = MutableStateFlow("WORD")
    val selectedTab: StateFlow<String> = _selectedTab

    fun setSelectedTab(tab: String) {
        _selectedTab.value = tab
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
    // Settings Ends

    val currentDua: List<Dua>
        get() = groupedAndSortedDuas[duaKeys.getOrNull(currentIndex)] ?: emptyList()

    fun nextDua() {
        if (currentIndex < duaKeys.lastIndex) {
            stopAudio()
            currentIndex++
        }
    }

    fun previousDua() {
        if (currentIndex > 0) {
            stopAudio()
            currentIndex--
        }
    }

    fun playFullAudio() {
        stopAudio()

        // If currentIndex is beyond the keys size, stop playback and reset
        if (currentIndex >= duaKeys.size) {
            isPlayingFullAudio = false
            highlightedIndex = -1
            currentIndex = 0 // reset for next time
            return
        }

        val duaList = currentDua // depends on currentIndex internally

        // If the list is empty or has no valid audio, skip to next group
        if (duaList.isEmpty()) {
            if (autoNextEnabled.value) {
                currentIndex++
                playFullAudio()
            }
            return
        }

        // Check first item's audio for validity (assuming all in group have audio)
        val firstDuaAudio = duaList.firstOrNull()?.fullAudioResId ?: 0
        if (firstDuaAudio == 0) {
            // No audio, skip to next group if enabled
            if (autoNextEnabled.value) {
                currentIndex++
                playFullAudio()
            }
            return
        }

        // Play audios in the current group starting from index 0
        playNextAudio(duaList, 0)
    }

    private fun playNextAudio(duaList: List<Dua>, index: Int) {
        if (index >= duaList.size) {
            // Finished current group
            isPlayingFullAudio = false
            highlightedIndex = -1

            if (autoNextEnabled.value) {
                currentIndex++
                playFullAudio()
            }
            return
        }

        val resId = duaList[index].fullAudioResId
        if (resId == 0) {
            // Skip invalid audio, play next in current group
            playNextAudio(duaList, index + 1)
            return
        }

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer?.apply {
                start()
                setOnCompletionListener {
                    release()
                    mediaPlayer = null
                    playNextAudio(duaList, index + 1)
                }
            }

            isPlayingFullAudio = true
            highlightedIndex = index

        } catch (e: Exception) {
            e.printStackTrace()
            // If error occurs, try next audio
            playNextAudio(duaList, index + 1)
        }
    }

    fun playWordByWord() {
        stopAudio()
        globalWordIndex = 0
        // If currentIndex is beyond available dua groups, reset and stop
        if (currentIndex >= duaKeys.size) {
            isPlayingWordByWord = false
            highlightedIndex = -1
            currentIndex = 0
            return
        }

        val duaList = currentDua // This comes from duaKeys[currentIndex]
        val allWordAudioPairs = duaList.flatMap { it.wordAudioPairs }

        if (allWordAudioPairs.isEmpty()) {
            // Skip to next group if autoNextEnabled
            if (autoNextEnabled.value) {
                currentIndex++
                playWordByWord()
            }
            return
        }

        isPlayingWordByWord = true
        highlightedIndex = -1

        playWordsSequentially(allWordAudioPairs, 0)
    }

    private fun playWordsSequentially(pairs: List<Pair<String, Int>>, index: Int) {
        if (index >= pairs.size) {
            isPlayingWordByWord = false
            highlightedIndex = -1

            // After finishing current group, go to next group if autoNext is on
            if (autoNextEnabled.value) {
                currentIndex++
                globalWordIndex = 0 // ✅ Reset when moving to next dua group
                playWordByWord()
            }else{
                globalWordIndex = -1
            }

            return
        }

        highlightedIndex = index
        globalWordIndex = index

        val audioResId = pairs[index].second
        if (audioResId == 0) {
            playWordsSequentially(pairs, index + 1)
            return
        }

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, audioResId)

            mediaPlayer?.apply {
                start()
                setOnCompletionListener {
                    release()
                    mediaPlayer = null
                    playWordsSequentially(pairs, index + 1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            playWordsSequentially(pairs, index + 1)
        }
    }



    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlayingFullAudio = false
        isPlayingWordByWord = false
        highlightedIndex = -1
    }


    private fun getResId(resourceId: String): Int {
        return context.resources.getIdentifier(resourceId, "raw", context.packageName)
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }

    // Toggle favorite
    fun toggleFavorite(duaNumber: String, status: String) {
        viewModelScope.launch {
            val isFav = dao.isFavorite(duaNumber)
            val dua = DuaStatusEntity(duaNumber, !isFav, status)
            if (isFav) {
                dao.updateFavoriteStatus(duaNumber, false)
            } else {
                dao.insertFavorite(dua)
            }
        }
    }

    // Check if a dua is marked as favorite
    suspend fun isFavorite(duaNumber: String): Boolean {
        return dao.isFavorite(duaNumber)
    }

    // Update status (e.g., Memorized, In Practice, Not Started)
    fun updateDuaStatus(duaNumber: String, newStatus: String) {
        viewModelScope.launch {
            val existingDua = dao.getDuaStatusByNumber(duaNumber)
            if (existingDua != null) {
                dao.updateDuaStatus(duaNumber, newStatus)
            } else {
                // Insert with default favorite = false
                val newDua = DuaStatusEntity(
                    duaNumber = duaNumber,
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
                val existing = dao.getDuaStatusByNumber(dua.duaNumber)
                if (existing == null) {
                    dao.insertFavorite(
                        DuaStatusEntity(
                            duaNumber = dua.duaNumber,
                            favorite = false,
                            status = "In Practice"
                        )
                    )
                }
            }
        }
    }


    // Get Duas by specific status
    fun getDuasByStatus(status: String): StateFlow<List<DuaStatusEntity>> {
        return dao.getDuasByStatus(status)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    // Get only favorite Duas by status
    fun getFavoriteDuasByStatus(status: String): StateFlow<List<DuaStatusEntity>> {
        return dao.getFavoriteDuasByStatus(status)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
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

}

