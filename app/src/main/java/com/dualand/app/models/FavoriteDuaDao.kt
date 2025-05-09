package com.dualand.app.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDuaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(dua: FavoriteDuaEntity)

    @Delete
    suspend fun removeFavorite(dua: FavoriteDuaEntity)

    @Query("SELECT * FROM favorite_duas")
    fun getAllFavorites(): Flow<List<FavoriteDuaEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_duas WHERE duaNumber = :duaNumber)")
    suspend fun isFavorite(duaNumber: String): Boolean

}
