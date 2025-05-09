package com.dualand.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dualand.app.models.Dua
import com.dualand.app.models.FavoriteDuaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class DuaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).favoriteDuaDao()
    val favoriteDuas: StateFlow<List<FavoriteDuaEntity>> = dao.getAllFavorites().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun toggleFavorite(duaNumber: String, textHeading: String, imageResId: Int) {
        viewModelScope.launch {
            val isFav = dao.isFavorite(duaNumber)
            val dua = FavoriteDuaEntity(duaNumber, textHeading, imageResId)
            if (isFav) {
                dao.removeFavorite(dua)
            } else {
                dao.insertFavorite(dua)
            }
        }
    }

    suspend fun isFavorite(duaNumber: String): Boolean {
        return dao.isFavorite(duaNumber)
    }
}

