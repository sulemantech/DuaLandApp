package com.dualand.app

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dualand.app.activities.DuaDataProvider
import com.dualand.app.activities.DuaDataProvider.duaList
import com.dualand.app.activities.MediaPlayerManager.playAudio
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

    var globalWordIndex by mutableStateOf(-1)


    private val context = application.applicationContext

    val someSettingState = mutableStateOf(false)

    fun toggleSomeSetting() {
        someSettingState.value = !someSettingState.value
    }

    var currentIndex by mutableStateOf(0)
        private set

    fun updateCurrentIndex(index: Int) {
        currentIndex = index
    }

    var highlightedIndex by mutableStateOf(-1)
        private set

    var isPlayingWordByWord by mutableStateOf(false)
        private set

    var isPlayingFullAudio by mutableStateOf(false)
        private set

    private var mediaPlayer: MediaPlayer? = null

    // Group and sort the dua list
//    private val groupedAndSortedDuas = DuaDataProvider.duaList.groupBy {
//        it.duaNumber.substringBefore(".").trim().toInt()
//    }.toSortedMap()

    private val _groupedAndSortedDuas = DuaDataProvider.duaList.groupBy {
        it.duaNumber.substringBefore(".").trim().toInt()
    }.toSortedMap()

    // Expose as public read-only property
    val groupedAndSortedDuas: SortedMap<Int, List<Dua>>
        get() = _groupedAndSortedDuas

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
    val currentFullAudioDuaGroupIndex = mutableStateOf(-1)

    fun playFullAudio() {
        stopAudio()
        Log.d("AudioPlay", "playFullAudio called with currentIndex=$currentIndex")

        if (currentIndex >= duaKeys.size) {
            Log.d("AudioPlay", "Reached end of duaKeys, resetting states")
            isPlayingFullAudio = false
            highlightedIndex = -1
            currentFullAudioDuaGroupIndex.value = -1
            currentIndex = 0
            return
        }

        val duaList = currentDua
        Log.d("AudioPlay", "duaList size=${duaList.size}")

        if (duaList.isEmpty()) {
            if (autoNextEnabled.value) {
                currentIndex++
                playFullAudio()
            }
            return
        }

        val titleAudio = duaList.firstOrNull()?.titleAudioResId ?: 0
        if (readTitleEnabled.value && titleAudio != 0) {
            try {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(context, titleAudio)
                mediaPlayer?.apply {
                    start()
                    Log.d("AudioPlay", "Playing title audio for group $currentIndex")
                    setOnCompletionListener {
                        release()
                        mediaPlayer = null
                        playNextAudio(duaList, 0, currentIndex)
                    }
                }

                isPlayingFullAudio = true
                highlightedIndex = -1
                currentFullAudioDuaGroupIndex.value = currentIndex

            } catch (e: Exception) {
                e.printStackTrace()
                playNextAudio(duaList, 0, currentIndex)
            }
        } else {
            playNextAudio(duaList, 0, currentIndex)
        }
    }

    private fun playNextAudio(duaList: List<Dua>, index: Int, duaGroupIndex: Int) {
        if (index >= duaList.size) {
            // Finished current group
            isPlayingFullAudio = false
            highlightedIndex = -1

            Handler(Looper.getMainLooper()).post {
                currentFullAudioDuaGroupIndex.value = -1
            }

            if (autoNextEnabled.value) {
                currentIndex++
                playFullAudio()
            }
            return
        }

        val resId = duaList[index].fullAudioResId
        if (resId == 0) {
            // Skip invalid audio, play next in current group
            playNextAudio(duaList, index + 1, duaGroupIndex)
            return
        }

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer?.apply {
                start()

                // Update state immediately on main thread so UI updates icon properly
                Handler(Looper.getMainLooper()).post {
                    currentFullAudioDuaGroupIndex.value = duaGroupIndex
                    isPlayingFullAudio = true
                    highlightedIndex = index
                }

                setOnCompletionListener {
                    release()
                    mediaPlayer = null
                    playNextAudio(duaList, index + 1, duaGroupIndex)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            playNextAudio(duaList, index + 1, duaGroupIndex)
        }
    }

    var currentDuaIndex by mutableStateOf(0)
    var currentWordIndexInDua by mutableStateOf(-1)

    fun playWordByWord() {
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
            playWordByWord()
            return
        }

        isPlayingWordByWord = true
        currentDuaIndex = 0

        val firstDua = duaList.firstOrNull()
        val titleAudioResId = firstDua?.titleAudioResId

        if (readTitleEnabled.value && titleAudioResId != null) {
            playAudio(titleAudioResId) {
                playDuaSequentially(duaList, 0)
            }
        } else {
            playDuaSequentially(duaList, 0)
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
    val currentDuaIndexState = mutableStateOf(-1) // 👈 Observable

    private fun playDuaSequentially(duaList: List<Dua>, duaIndex: Int) {
        if (duaIndex >= duaList.size) {
            isPlayingWordByWord = false
            currentWordIndexInDua = -1
            currentDuaIndexState.value = -1

            if (autoNextEnabled.value) {
                currentIndex++
                playWordByWord()
            }

            return
        }
        currentDuaIndexState.value = duaIndex // 👈 Set for UI binding
        currentDuaIndex = duaIndex
        currentWordIndexInDua = 0

        val currentDua = duaList[duaIndex]
        val wordAudioPairs = currentDua.wordAudioPairs

        playWordsInDua(wordAudioPairs, 0) {
            playDuaSequentially(duaList, duaIndex + 1)
        }
    }

    val isListeningPause = mutableStateOf(false)

    private fun playWordsInDua(
        pairs: List<Pair<String, Int>>,
        index: Int,
        onFinished: () -> Unit
    ) {
        // Stop if index exceeds or user manually stopped
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
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, audioResId)
            mediaPlayer?.apply {
                start()
                setOnCompletionListener {
                    val duration = duration.toLong()

                    release()
                    mediaPlayer = null

                    isListeningPause.value = true

                    Handler(Looper.getMainLooper()).postDelayed({
                        // ⛔ Check again before continuing
                        if (!isPlayingWordByWord) {
                            isListeningPause.value = false
                            currentWordIndexInDua = -1
                            return@postDelayed
                        }

                        isListeningPause.value = false
                        playWordsInDua(pairs, index + 1, onFinished)
                    }, duration)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            playWordsInDua(pairs, index + 1, onFinished)
        }
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        isPlayingFullAudio = false
        isPlayingWordByWord = false
        highlightedIndex = -1
        currentWordIndexInDua = -1
        currentDuaIndex = -1
        currentFullAudioDuaGroupIndex.value = -1
        isListeningPause.value = false
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


    fun toggleFavoriteStatus(dua: Dua) {
        viewModelScope.launch {
            val duaNumber = dua.duaNumber
            val currentStatus = dao.isFavorite(duaNumber)
            dao.updateFavoriteStatus(duaNumber, !currentStatus)
        }
    }

    fun isFavorite(duaNumber: String): Flow<Boolean> {
        return dao.getAllFavoriteDuas().map { list ->
            list.any { it.duaNumber == duaNumber }
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

    // Track word-by-word play state per dua
    private val _isPlayingWordByWordMap = mutableStateMapOf<Int, Boolean>()
    val isPlayingWordByWordMap: Map<Int, Boolean> get() = _isPlayingWordByWordMap

    // Track full audio play state per dua
    private val _isPlayingFullAudioMap = mutableStateMapOf<Int, Boolean>()
    val isPlayingFullAudioMap: Map<Int, Boolean> get() = _isPlayingFullAudioMap

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
}

