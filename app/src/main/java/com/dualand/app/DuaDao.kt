package com.dualand.app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DuaDao {
    @Query("SELECT * FROM duas WHERE isFavorite = 1")
    fun getFavoriteDuas(): Flow<List<Dua>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDua(dua: Dua)

    @Update
    suspend fun updateDua(dua: Dua)
}
