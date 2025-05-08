package com.dualand.app

import kotlinx.coroutines.flow.Flow

class DuaRepository(private val dao: DuaDao) {

    // Flow of favorite DUAs
    val favoriteDuas: Flow<List<DuaFav>> = dao.getFavoriteDuas()

    // Method to update a favorite DUA
    suspend fun updateDua(dua: DuaFav) {
        dao.updateDua(dua)  // Update the DuaFav object in the database
    }
}

