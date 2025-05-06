package com.dualand.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DuaViewModel(application: Application) : AndroidViewModel(application) {
    private val duaDao = AppDatabase.getDatabase(application).duaDao()
    val favoriteDuas: StateFlow<List<DuaFav>> = duaDao.getFavoriteDuas().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    fun toggleFavorite(dua: DuaFav) {
        viewModelScope.launch {
            val updated = dua.copy(isFavorite = !dua.isFavorite)
            duaDao.insertDua(updated)
        }
    }
}
