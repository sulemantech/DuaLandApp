package com.dualand.app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DuaDao {

    @Query("SELECT * FROM dua_fav WHERE isFavorite = 1")
    fun getFavoriteDuas(): Flow<List<DuaFav>>

    @Update
    suspend fun updateDua(dua: DuaFav)

    @Delete
    suspend fun deleteDua(dua: DuaFav)

}

