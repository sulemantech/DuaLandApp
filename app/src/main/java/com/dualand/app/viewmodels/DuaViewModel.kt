package com.dualand.app

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dualand.app.models.Dua
import com.dualand.app.models.DuaStatusEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DuaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).favoriteDuaDao()

//    private val _selectedTab = mutableStateOf("WORD")
//    val selectedTab: State<String> = _selectedTab
//
//    fun setSelectedTab(tab: String) {
//        _selectedTab.value = tab
//    }

    private val favoriteIndicesQueue = mutableListOf<Int>()
    private var isPlayingFavorites = false
    private val _currentDuaIndex = MutableStateFlow<Int?>(null)
    val currentDuaIndex: StateFlow<Int?> = _currentDuaIndex
    private var favoritePlaybackIndex = 0 // Index in favoriteIndicesQueue

    fun startPlayingFavorites(indices: List<Int>) {
        if (indices.isEmpty()) return
        favoriteIndicesQueue.clear()
        favoriteIndicesQueue.addAll(indices)
        isPlayingFavorites = true
        favoritePlaybackIndex = 0
        _currentDuaIndex.value = favoriteIndicesQueue[favoritePlaybackIndex]
    }

//    fun startPlayingFavorites(indices: List<Int>) {
//        if (indices.isEmpty()) return
//        favoriteIndicesQueue.clear()
//        favoriteIndicesQueue.addAll(indices)
//        isPlayingFavorites = true
//        playNextFavorite()
//    }
    fun getCurrentDuaIndex(): Int? {
        return _currentDuaIndex.value
    }


    fun playNextFavorite() {
        if (favoriteIndicesQueue.isNotEmpty()) {
            val nextIndex = favoriteIndicesQueue.removeAt(0)
            _currentDuaIndex.value = nextIndex // trigger navigation
        } else {
            isPlayingFavorites = false
        }
    }

    fun isPlayingFavoritesFlow(): Boolean = isPlayingFavorites

    // Get all favorites
    val favoriteDuas: StateFlow<List<DuaStatusEntity>> = dao.getAllFavoriteDuas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Get all statuses
    val allDuas: StateFlow<List<DuaStatusEntity>> = dao.getAllDuaStatuses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
    fun convertInPracticeToMemorized() {
        viewModelScope.launch {
            allDuas.value.forEach { duaStatus ->
                if (duaStatus.status == "In Practice") {
                    updateDuaStatus(
                        duaNumber = duaStatus.duaNumber,
                        newStatus = "Memorized"
                    )
                }
            }
        }
    }

}

