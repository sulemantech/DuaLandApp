package com.dualand.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dualand.app.models.DuaStatusEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DuaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).favoriteDuaDao()

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
            dao.updateDuaStatus(duaNumber, newStatus)
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

