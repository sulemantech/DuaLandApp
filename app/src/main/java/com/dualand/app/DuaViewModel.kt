package com.dualand.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dualand.app.activities.duaList
import com.dualand.app.models.Dua
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DuaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DuaRepository

    // Observing favorite duas as a StateFlow of List<DuaFav>
    val favoriteDuas: StateFlow<List<DuaFav>>

    init {
        val dao = DuaDatabase.getDatabase(application).duaDao()
        repository = DuaRepository(dao)

        // Observing the list of favorite duas from the repository
        favoriteDuas = repository.favoriteDuas
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    // This method is used to toggle the favorite status of a DuaFav
    fun toggleFavorite(dua: DuaFav) {
        viewModelScope.launch {
            val updatedDua = dua.copy(isFavorite = !dua.isFavorite)  // Toggle isFavorite
            repository.updateDua(updatedDua)  // Update the DUA in the repository
        }
    }
    val favList = duaList.map { dua ->
        DuaFav(
            id = dua.id,
            title = dua.textheading,
            image = dua.image,
            textheading = dua.textheading,
            duaNumber = dua.duaNumber,
            isFavorite = true
        )
    }

}

